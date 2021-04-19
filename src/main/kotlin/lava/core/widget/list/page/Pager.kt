package lava.core.widget.list.page

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lava.core.design.viewmodel.ViewModelX
import lava.core.ext.just
import lava.core.ext.launchIO
import lava.core.ext.launchMain
import lava.core.live.LiveBinding
import lava.core.live.decorPlugin
import lava.core.live.errorPlugin
import lava.core.live.loadingPlugin
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

    //    private var loadMorePlugin: ILoadMore? = null
//    private var refreshPlugin: IRefresh? = null
    private var layerPlugin: ILoadingView? = null
    private var errorPlugin: IErrorView? = null

    private var curLoader: ILoader? = null

    private var value = mutableListOf<DATA>()

    override fun onLifeCycleChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (Lifecycle.Event.ON_DESTROY == event) {
            loadingJob?.cancel()
        }
    }

    fun attach(plugin: ILoadMore): LivePagerX<DATA> {
        plugin.onLoadMore {
            curLoader = plugin
            nextPage()
        }
        return this
    }

    fun attach(plugin: IRefresh): LivePagerX<DATA> {
        plugin.onRefresh {
            curLoader = plugin
            refresh()
        }
        return this
    }

    fun attach(plugin: ILoadingView): LivePagerX<DATA> {
        layerPlugin = plugin
        plugin.onCancel {
            curLoader = plugin
            loadingJob?.cancel()
        }
        return this
    }

    fun attach(plugin: IErrorView): LivePagerX<DATA> {
        errorPlugin = plugin
        plugin.onRetry {
            curLoader = plugin
            doRequest()
        }
        return this
    }

    fun attach(plugin: IDecorView): LivePagerX<DATA> {
        plugin.onStart {
            curLoader = plugin
            nextPage()
        }
        return this
    }

    fun attach(vm: ViewModelX): LivePagerX<DATA> {
        attach(vm.loadingPlugin)
        attach(vm.errorPlugin)
        attach(vm.decorPlugin)
        return this
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
                rsp.list.just { value.addAll(this) }
                postValue(value)
                if (rsp.list.isNullOrEmpty() || rsp.list.size < size) {
                    updateState(LoadingState.DONE)
                } else {
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
        scope?.launchMain {
            if (page == 1) {
                layerPlugin?.updateState(state)
                errorPlugin?.updateState(state)
            } else {
                curLoader?.updateState(state)
            }
            if (state == LoadingState.READY) {
                page++
            }
        }
    }

    abstract fun parse(input: Any): ParseResult<DATA>

    @Synchronized
    protected open fun nextPage() {
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