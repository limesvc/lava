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

typealias DataBuilder<IN> = (page: Int, size: Int) -> IN
typealias SuspendDataBuilder<IN> = suspend (page: Int, size: Int) -> IN

typealias LiveObserve<IN> = (IN) -> Unit

abstract class LivePagerX<IN, DATA>(private var page: Int = 1, private var size: Int = 20) :
    LiveData<List<DATA>>() {
    private var state = LoadingState.READY

    private var dataRequest: DataBuilder<IN?>? = null
    private var asyncDataRequest: SuspendDataBuilder<IN?>? = null

    private lateinit var scope: CoroutineScope
    private var loadingJob: Job? = null

    private var footer: LoadingFooter? = null

    private var onChange: LiveObserve<List<DATA>>? = null

    open fun with(owner: LifecycleOwner, scope: CoroutineScope): LivePagerX<IN, DATA> {
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

        this.scope = scope
        return this
    }

    fun attach(footer: LoadingFooter) {
        this.footer = footer
        footer.onLoad(::nextPage)
    }

    open fun reset() {
        page = 0
    }

    fun dataBuilder(block: DataBuilder<IN?>): LivePagerX<IN, DATA> {
        dataRequest = block
        return this
    }

    fun asyncDataBuilder(block: SuspendDataBuilder<IN?>): LivePagerX<IN, DATA> {
        asyncDataRequest = block
        return this
    }

    fun observeOnce(observer: LiveObserve<List<DATA>>) {
        onChange = observer
    }

    private fun doRequest() {
        updateState(LoadingState.LOADING)
        loadingJob = scope.launchIO {
            kotlin.runCatching {
                val input = dataRequest?.invoke(page, size) ?: asyncDataRequest?.invoke(page, size)
                setup(input)
            }.onFailure {
                L.e(it)
                updateState(LoadingState.ERROR)
            }.getOrNull()
        }
    }

    private fun setup(input: IN?) {
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
            scope.launchMain {
                it.updateState(state)
            }
        }
    }

    abstract fun parse(input: IN): Response<DATA>

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

class LivePager<DATA> : LivePagerX<Any, DATA>() {
    override fun parse(input: Any): Response<DATA> {
        val rsp = ParserFactory.parse<DATA>(input)
        return rsp ?: Response(false)
    }
}