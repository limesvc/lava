package lava.core.design.view

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import lava.core.design.viewmodel.ViewModelX

abstract class ActivityV<VM : ViewModelX> : ActivityX() {
    lateinit var vm: VM

    abstract fun binding(): ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding().root)
        initView()
        initEvent()
        vm.onStart()
    }

    protected fun initView() {}

    protected fun initEvent() {}
}