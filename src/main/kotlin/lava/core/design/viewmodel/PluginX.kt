package lava.core.design.viewmodel

import lava.core.design.view.struct.OnRetry
import lava.core.design.view.struct.StructState
import lava.core.live.decorPlugin
import lava.core.live.errorPlugin
import lava.core.live.loadingPlugin
import lava.core.net.LoadingState
import lava.core.type.Block
import lava.core.widget.list.page.IVMPlugin

fun ViewModelX.onRetry(block: Block) {
    registerPlugin(OnRetry, object : IVMPlugin {
        override fun <T> onViewStateChange(state: StructState<T>) {
            block()
        }
    })
}

fun ViewModelX.asLoading() {
    loadingPlugin.updateState(LoadingState.LOADING)
    errorPlugin.updateState(LoadingState.LOADING)
}

fun ViewModelX.asLoaded() {
    errorPlugin.updateState(LoadingState.DONE)
    decorPlugin.updateState(LoadingState.DONE)
    loadingPlugin.updateState(LoadingState.DONE)
}

fun ViewModelX.asError() {
    errorPlugin.updateState(LoadingState.ERROR)
    decorPlugin.updateState(LoadingState.ERROR)
    loadingPlugin.updateState(LoadingState.ERROR)
}