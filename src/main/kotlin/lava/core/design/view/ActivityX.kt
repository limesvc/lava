package lava.core.design.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class ActivityX : AppCompatActivity() {
    protected fun <T:Activity> startActivity(clazz: KClass<T>){
        startActivity(Intent(this, clazz.java))
    }
}