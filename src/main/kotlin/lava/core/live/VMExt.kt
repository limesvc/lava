package lava.core.live

import lava.core.bus.Flag
import lava.core.design.view.struct.ErrorStruct
import lava.core.design.view.struct.LoadingStruct
import lava.core.design.view.struct.StructStatus
import lava.core.design.viewmodel.ViewModelX
import lava.core.net.LoadingState
import lava.core.type.Block
import lava.core.widget.list.page.IErrorView
import lava.core.widget.list.page.ILoadingView

/**
 * Created by svc on 2021/1/28
 */
const val VM_LOADING_PLUGIN = "lava.core.live.vm_loading_plugin"
const val VM_LOADING_FLAG = "lava.core.live.vm_loading_flag"

val ViewModelX.flagLoadingState: Flag<LoadingState>
    get() = getOrCreate(VM_LOADING_FLAG) {
        Flag(-1000)
    }

val ViewModelX.loadingPlugin: ILoadingView
    get() = getOrCreate(VM_LOADING_PLUGIN) {
        object : ILoadingView {
            override fun onCancel(block: Block) {
            }

            override fun updateState(state: LoadingState) {
                sendUI(flagLoadingState.flag, state)
            }
        }
    }

const val VM_ERROR_PLUGIN = "lava.core.live.vm_error_plugin"
const val VM_ERROR_FLAG = "lava.core.live.vm_error_flag"

val ViewModelX.flagErrorState: Flag<LoadingState>
    get() = getOrCreate(VM_ERROR_FLAG) {
        Flag(-1001)
    }

val ViewModelX.errorPlugin: IErrorView
    get() = getOrCreate(VM_ERROR_PLUGIN) {
        object : IErrorView {
            override fun onRetry(block: Block) {
            }

            override fun updateState(state: LoadingState) {
                sendUI(flagErrorState.flag, state)
            }
        }
    }