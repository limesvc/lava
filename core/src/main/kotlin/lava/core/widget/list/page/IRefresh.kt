package lava.core.widget.list.page

import lava.core.design.view.struct.StructState
import lava.core.net.LoadingState
import lava.core.type.Block

/**
 * Created by svc on 2021/1/26
 */
interface ILoader {
    fun updateState(state: LoadingState)
}

interface IRefresh : ILoader {
    fun onRefresh(block: Block)
}

interface ILoadMore : ILoader {
    fun onLoadMore(block: Block)
}

interface ILoadingView : ILoader {
    fun onCancel(block: Block)
}

interface IErrorView : ILoader {
    fun onRetry(block: Block)
}

interface IDecorView : ILoader {
    fun onStart(block: Block)
}

interface IVMPlugin {
    fun handled() = true
    fun <T> onViewStateChange(state: StructState<T>)
}