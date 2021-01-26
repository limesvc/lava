package lava.core.design.viewmodel

import androidx.lifecycle.ViewModel
import lava.core.bus.*

abstract class ViewModelX : ViewModel() {
    private val bus = VMBus()

    protected infix fun <T> Flag<T>.set(value: T) {
        bus.send(flag, value)
    }

    protected operator fun <T> Flag<T>.invoke(value: T? = null) {
        bus.send(flag, value)
    }

    fun connect(block: LiveBus.() -> Unit) {
        bus.block()
    }

    fun postUI(flag: Int, any: Any? = 0) = bus.post(flag, any)

    fun sendUI(flag: Int, any: Any? = 0) = bus.send(flag, any)

    open fun onStart() {}
}