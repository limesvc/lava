package lava.core.design.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import lava.core.bus.Bus
import lava.core.bus.LiveBus
import lava.core.design.view.struct.*
import lava.core.design.viewmodel.ViewModelX
import lava.core.ext.just
import lava.core.live.flagDecorState
import lava.core.live.flagErrorState
import lava.core.live.flagLoadingState
import lava.core.util.getGenericClass

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
        vm.connect { bindVM() }
        vm.onInit()
        super.onSetup(contentView, binding)
        vm.onStart()
    }

    protected open fun Bus.bindStruct() {
        struct[DecorStruct]?.just {
            on(vm.flagDecorState) {
                updateState(it)
            }
        }

        struct[LoadingStruct]?.just {
            on(vm.flagLoadingState) {
                updateState(it)
            }
        }

        struct[ErrorStruct]?.just {
            on(vm.flagErrorState) {
                updateState(it)
            }
        }
    }

    override fun onBackPressed() {
        if (vm.onViewStateChanged(OnBackPressed)) {
            super.onBackPressed()
        }
    }

    override fun <T> onViewStateChanged(state: StructState<T>): T {
        return vm.onViewStateChanged(state)
    }

    private fun LiveBus.bindVM() {
        with(this@ActivityV).bindStruct()
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}
}