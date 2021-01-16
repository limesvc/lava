package lava.core.design.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import lava.core.design.view.struct.StructView
import kotlin.reflect.KClass

abstract class ActivityX : AppCompatActivity() {
    protected lateinit var viewBinding: ViewDataBinding

    protected fun <T : Activity> startActivity(clazz: KClass<T>) {
        startActivity(Intent(this, clazz.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = binding()
        val structView = getStructView()
        structView.build(::binding)
//        setContentView(binding.root)
    }

    abstract fun binding(): ViewDataBinding

    abstract fun getStructView(): StructView
}