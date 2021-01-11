package lava.core.design.viewmodel

import androidx.lifecycle.ViewModel
import lava.core.bus.LiveBus
import lava.core.bus.VMBus

abstract class ViewModelX : ViewModel() {
    private val bus = VMBus()

    fun connect(block: LiveBus.() -> Unit) {
        bus.block()
    }

    fun postUI(flag: Int, any: Any? = 0) = bus.post(flag, any)

    fun sendUI(flag: Int, any: Any? = 0) = bus.send(flag, any)

    open fun onStart() {}
}