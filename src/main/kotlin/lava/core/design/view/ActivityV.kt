package lava.core.design.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import lava.core.bus.Bus
import lava.core.bus.LiveBus
import lava.core.design.view.struct.StructState
import lava.core.design.viewmodel.ViewModelX
import lava.core.ext.just
import lava.core.live.VM_LOADING_PLUGIN
import lava.core.live.flagLoadingState
import lava.core.util.getGenericClass
import lava.core.widget.list.page.ILoadingView

abstract class ActivityV<VM : ViewModelX> : ActivityX() {
    private val vmProvider
        get() = ViewModelProvider(this, defaultViewModelProviderFactory)

    lateinit var vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        vmProvider.get(getGenericClass(this::class.java) as Class<VM>).also { vm = it }
        super.onCreate(savedInstanceState)
    }

    override fun onSetup(contentView: View, binding: ViewDataBinding) {
        super.onSetup(contentView, binding)
        vm.connect { bindVM() }
        vm.onStart()
    }

    private fun Bus.bindStruct() {
        vm.get<ILoadingView>(VM_LOADING_PLUGIN)?.just {
            on(vm.flagLoadingState) {
                updateState(it)
            }
        }
    }

    override fun <T> onViewStateChanged(state: StructState<T>): T {
        return vm.onViewStateChanged(state)
    }

    private fun LiveBus.bindVM() {
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}
}