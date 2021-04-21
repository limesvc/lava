package lava.core.design.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import lava.core.design.view.struct.DecorStruct
import lava.core.design.view.struct.DecorView
import lava.core.design.view.struct.StructHost
import lava.core.design.view.struct.StructView
import lava.core.util.inject
import kotlin.reflect.KClass

abstract class ActivityX : AppCompatActivity(), StructHost {
    protected lateinit var binding: ViewDataBinding
    protected lateinit var struct: StructView

    protected fun <T : Activity> startActivity(clazz: KClass<T>) {
        startActivity(Intent(this, clazz.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
        struct = getStructView()
        struct.build(this)
        struct[DecorStruct]?.onLifeCycleEvent(Lifecycle.Event.ON_CREATE)
    }

    protected fun setBackgroundColor(@ColorInt color: Int) {
        struct[DecorStruct]?.setBackgroundColor(color)
    }

    override fun onResume() {
        super.onResume()
        struct[DecorStruct]?.onLifeCycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onSetup(contentView: View, binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        setContentView(contentView)
        initView(binding)
        initEvent()
    }

    override fun getStructView(): StructView {
        return DecorView()
    }

    protected open fun initView(binding: ViewDataBinding) {}

    protected open fun initEvent() {}
}