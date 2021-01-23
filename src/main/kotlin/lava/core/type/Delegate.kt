package lava.core.type

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

fun <T> delegate(target: KMutableProperty0<T?>) = object :ReadWriteProperty<Any?, T?>{
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return target.invoke()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        target.set(value)
    }
}