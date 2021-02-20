package lava.core.design.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import lava.core.design.view.struct.*
import kotlin.reflect.KClass

abstract class ActivityX : AppCompatActivity(), StructHost {
    protected lateinit var binding: ViewDataBinding
    protected lateinit var struct: StructView

    protected fun <T : Activity> startActivity(clazz: KClass<T>) {
        startActivity(Intent(this, clazz.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStructView().build(this)
    }

    override fun onSetup(contentView: View, binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        setContentView(contentView)
        initView(binding)
        initEvent()
    }

    override fun <T> onViewStateChanged(state: StructState<T>): T {
        return null!!
    }

    override fun getStructView(): StructView {
        return DecorView() + LoadingView()
    }

    protected open fun initView(binding: ViewDataBinding) {}

    protected open fun initEvent() {}

    override fun onBackPressed() {
        super.onBackPressed()
    }
}