package lava.core.design.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import lava.core.design.view.struct.CommonDecorView
import lava.core.design.view.struct.StructHost
import lava.core.design.view.struct.StructView
import kotlin.reflect.KClass

abstract class ActivityX : AppCompatActivity(), StructHost {
    protected lateinit var binding: ViewDataBinding

    protected fun <T : Activity> startActivity(clazz: KClass<T>) {
        startActivity(Intent(this, clazz.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStructView().build(this)
    }

    override fun onSetup(binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        setContentView(binding.root)
        initView(binding)
        initEvent()
    }

    override fun getStructView(): StructView {
        return CommonDecorView()
    }

    protected open fun <T> initView(t: T) {}

    protected open fun initEvent() {}
}