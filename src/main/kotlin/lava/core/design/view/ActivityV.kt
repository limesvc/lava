package lava.core.design.view

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("UNCHECKED_CAST")
        vmProvider.get(getGenericClass(this::class.java) as Class<VM>).also { vm = it }
        super.onCreate(savedInstanceState)
    }

    override fun onSetup(binding: ViewDataBinding) {
        super.onSetup(binding)
        vm.connect { bindVM() }
        vm.onStart()
    }

    protected fun LiveBus.bindVM() {
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}
}