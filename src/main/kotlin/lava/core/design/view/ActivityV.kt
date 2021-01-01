package lava.core.design.view

import android.os.Bundle
import lava.core.design.viewmodel.ViewModelX
import javax.inject.Inject

open class ActivityV<VM : ViewModelX> : ActivityX() {
    @Inject
    lateinit var vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvent()
        vm.onStart()
    }

    protected fun initView() {}

    protected fun initEvent() {}
}