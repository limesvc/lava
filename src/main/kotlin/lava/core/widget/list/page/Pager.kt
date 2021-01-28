package lava.core.widget.list.page

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import lava.core.ext.launchIO
import lava.core.ext.launchMain
import lava.core.live.LiveBinding
import lava.core.net.LoadingState
import logger.L

typealias DataBuilder = (page: Int, size: Int) -> Any?
typealias SuspendDataBuilder = suspend (page: Int, size: Int) -> Any?

typealias LiveObserve<IN> = (IN) -> Unit

abstract class LivePagerX<DATA>(protected var page: Int, protected var size: Int) :
    LiveBinding<List<DATA>>() {
    private var state = LoadingState.READY

    private var dataRequest: DataBuilder? = null
    private var asyncDataRequest: SuspendDataBuilder? = null

    private var scope: CoroutineScope? = null
    private var loadingJob: Job? = null

    private var loadMorePlugin: ILoadMore? = null
    private var refreshPlugin: IRefresh? = null
    private var layerPlugin: ILoadingView? = null

    private var onChange: LiveObserve<List<DATA>>? = null

    override fun onLifeCycleChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (Lifecycle.Event.ON_DESTROY == event) {
            loadingJob?.cancel()
        }
    }

    override fun onChanged(value: List<DATA>) {
        onChange?.invoke(value)
    }

    fun attach(plugin: ILoadMore) {
        loadMorePlugin = plugin
        plugin.onLoadMore(::nextPage)
    }

    fun attach(plugin: IRefresh) {
        refreshPlugin = plugin
        plugin.onRefresh(::refresh)
    }

    fun attach(plugin: ILoadingView) {
        layerPlugin = plugin
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
                    updateState(LoadingState.READY)
                    page++
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
        if (loadMorePlugin != null || refreshPlugin != null) {
            scope?.launchMain {
                if (page == 1 && layerPlugin != null) {
                    layerPlugin?.updateState(state)
                } else {
                    layerPlugin?.updateState(state)
                    refreshPlugin?.updateState(state)
                    loadMorePlugin?.updateState(state)
                }
            }
        }
    }

    abstract fun parse(input: Any): ParseResult<DATA>

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

    fun launch(scope: CoroutineScope) {
        this.scope = scope
        nextPage()
    }
}

class LivePager<DATA>(page: Int = 1, size: Int = 20) : LivePagerX<DATA>(page, size) {
    override fun parse(input: Any): ParseResult<DATA> {
        val rsp = ParserFactory.parse<DATA>(input)
        return rsp ?: ParseResult(false)
    }
}