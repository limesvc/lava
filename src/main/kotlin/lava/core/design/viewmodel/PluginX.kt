package lava.core.design.viewmodel

import lava.core.design.view.struct.OnRetry
import lava.core.design.view.struct.StructState
import lava.core.type.Block
import lava.core.widget.list.page.IVMPlugin

fun ViewModelX.onRetry(block: Block) {
    registerPlugin(OnRetry, object : IVMPlugin {
        override fun <T> onViewStateChange(state: StructState<T>) {
            block()
        }
    })
}