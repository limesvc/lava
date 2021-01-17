package lava.core.design.view

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import lava.core.bus.Bus
import lava.core.bus.LiveBus
import lava.core.design.viewmodel.ViewModelX
import lava.core.util.getGenericClass

abstract class ActivityV<VM : ViewModelX> : ActivityX() {
    private val vmProvider
        get() = ViewModelProvider(this, defaultViewModelProviderFactory)

    @Suppress
    protected lateinit var vm: VM

    override fun onSetup(binding: ViewDataBinding) {
        super.onSetup(binding)
        @Suppress("UNCHECKED_CAST")
        vmProvider.get(getGenericClass(this::class.java) as Class<VM>).also { vm = it }
        vm.connect { bindVM() }
        vm.onStart()
    }

    protected open fun LiveBus.bindVM() {
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}
}