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

    protected lateinit var vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = vmProvider.get(getGenericClass(this::class.java) as Class<VM>)

        val binding = binding()
        setContentView(binding.root)
        initView(binding)
        initEvent()
        vm.connect { bindVM() }
        vm.onStart()
    }

    abstract fun binding(): ViewDataBinding

    protected open fun LiveBus.bindVM() {
        with(this@ActivityV).linkVMLive()
    }

    protected open fun Bus.linkVMLive() {}

    protected open fun <T> initView(t: T) {}

    protected open fun initEvent() {}
}