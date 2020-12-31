package lava.core.sp

import android.content.Context
import android.content.SharedPreferences
import lava.core.appContext
import lava.core.ext.applyValue
import lava.core.obj.fromJson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by svc on 2020/12/30
 */

abstract class SP {
    protected open val name = ""

    protected val preferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(
            name,
            Context.MODE_PRIVATE
        )
    }

    protected fun bindBool(key: String, default: Boolean = false) =
        object : ReadWriteProperty<SP, Boolean> {
            override fun getValue(thisRef: SP, property: KProperty<*>) =
                preferences.getBoolean(key, default)

            override fun setValue(thisRef: SP, property: KProperty<*>, value: Boolean) {
                preferences.applyValue(key, value)
            }
        }

    protected fun bindString(key: String, default: String = "") =
        object : ReadWriteProperty<SP, String> {
            override fun getValue(thisRef: SP, property: KProperty<*>) =
                preferences.getString(key, default) ?: default

            override fun setValue(thisRef: SP, property: KProperty<*>, value: String) {
                preferences.applyValue(key, value)
            }
        }

    protected fun bindFloat(key: String, default: Float = 0f) =
        object : ReadWriteProperty<SP, Float> {
            override fun getValue(thisRef: SP, property: KProperty<*>) =
                preferences.getFloat(key, default)

            override fun setValue(thisRef: SP, property: KProperty<*>, value: Float) {
                preferences.applyValue(key, value)
            }
        }

    protected fun bindLong(key: String, default: Long = 0) = object : ReadWriteProperty<SP, Long> {
        override fun getValue(thisRef: SP, property: KProperty<*>) =
            preferences.getLong(key, default)

        override fun setValue(thisRef: SP, property: KProperty<*>, value: Long) {
            preferences.applyValue(key, value)
        }
    }

    protected fun bindInt(key: String, default: Int = 0) = object : ReadWriteProperty<SP, Int> {
        override fun getValue(thisRef: SP, property: KProperty<*>) =
            preferences.getInt(key, default)

        override fun setValue(thisRef: SP, property: KProperty<*>, value: Int) {
            preferences.applyValue(key, value)
        }
    }

    protected fun <T : Any> bindObject(key: String, clazz: KClass<T>) =
        object : ReadWriteProperty<SP, T> {
            override fun getValue(thisRef: SP, property: KProperty<*>) =
                clazz.fromJson(preferences.getString(key, "").orEmpty())

            override fun setValue(thisRef: SP, property: KProperty<*>, value: T) {
                preferences.applyValue(key, value)
            }
        }

    protected inline fun <reified T : Any> bindAny(key: String) =
        object : ReadWriteProperty<SP, T> {
            override fun getValue(thisRef: SP, property: KProperty<*>) =
                T::class.fromJson(preferences.getString(key, "").orEmpty())

            override fun setValue(thisRef: SP, property: KProperty<*>, value: T) {
                preferences.applyValue(key, value)
            }
        }

    var test by bindAny<Int>("222222222222")
}