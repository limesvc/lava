package lava.core.design.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import lava.core.bus.Bus
import lava.core.bus.LiveBus
import lava.core.design.viewmodel.ViewModelX
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
        super.onSetup(contentView, binding)
        vm.connect { bindVM() }
        vm.onStart()
    }

    protected fun LiveBus.bindVM() {
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}
}