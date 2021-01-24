package lava.core.list.page

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import lava.core.ext.launchIO
import lava.core.ext.launchMain
import lava.core.list.LoadingFooter
import lava.core.list.LoadingState
import logger.L

typealias DataBuilder = (page: Int, size: Int) -> Any?
typealias SuspendDataBuilder = suspend (page: Int, size: Int) -> Any?

typealias LiveObserve<IN> = (IN) -> Unit

abstract class LivePagerX<DATA>(owner: LifecycleOwner, protected var page: Int, protected var size: Int) :
    LiveData<List<DATA>>() {
    private var state = LoadingState.READY

    private var dataRequest: DataBuilder? = null
    private var asyncDataRequest: SuspendDataBuilder? = null

    private var scope: CoroutineScope? = null
    private var loadingJob: Job? = null

    private var footer: LoadingFooter? = null

    private var onChange: LiveObserve<List<DATA>>? = null

    init {
        attach(owner)
    }

    protected fun attach(owner: LifecycleOwner) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) return

        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    loadingJob?.cancel()
                    owner.lifecycle.removeObserver(this)
                }
            }
        })

        observe(owner, Observer {
            onChange?.invoke(it)
        })

        this.scope = owner.lifecycleScope
    }

    fun attach(footer: LoadingFooter) {
        this.footer = footer
        footer.onLoad(::nextPage)
    }

    open fun reset() {
        page = 0
    }

    fun dataBuilder(block: DataBuilder): LivePagerX<DATA> {
        dataRequest = block
        return this
    }

    fun asyncDataBuilder(block: SuspendDataBuilder): LivePagerX<DATA> {
        asyncDataRequest = block
        return this
    }

    fun observeOnce(observer: LiveObserve<List<DATA>>) {
        onChange = observer
    }

    private fun doRequest() {
        updateState(LoadingState.LOADING)
        loadingJob = scope?.launchIO {
            kotlin.runCatching {
                val input = dataRequest?.invoke(page, size) ?: asyncDataRequest?.invoke(page, size)
                setup(input)
            }.onFailure {
                L.e(it)
                updateState(LoadingState.ERROR)
            }.getOrNull()
        }
    }

    private fun setup(input: Any?) {
        if (input != null) {
            val rsp = parse(input)
            if (rsp.success) {
                postValue(rsp.list)
                if (rsp.list.isNullOrEmpty() || rsp.list.size < size) {
                    updateState(LoadingState.DONE)
                } else {
                    page++
                    updateState(LoadingState.READY)
                }
            } else {
                updateState(LoadingState.ERROR)
            }
        } else {
            updateState(LoadingState.ERROR)
        }
    }

    private fun updateState(state: LoadingState) {
        this.state = state
        footer?.also {
            scope?.launchMain {
                it.updateState(state)
            }
        }
    }

    abstract fun parse(input: Any): Response<DATA>

    @Synchronized
    open fun nextPage() {
        if (state == LoadingState.READY) {
            doRequest()
        }
    }

    @Synchronized
    open fun refresh() {
        if (state != LoadingState.LOADING) {
            reset()
            doRequest()
        }
    }

    fun cancel(cause: CancellationException? = null) {
        loadingJob?.cancel(cause)
        loadingJob = null
    }
}

class LivePager<DATA>(owner: LifecycleOwner, page: Int = 1, size: Int = 20) : LivePagerX<DATA>(owner, page, size) {
    override fun parse(input: Any): Response<DATA> {
        val rsp = ParserFactory.parse<DATA>(input)
        return rsp ?: Response(false)
    }
}