package lava.core.design.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import lava.core.bus.Bus
import lava.core.bus.VMBus

abstract class ViewModelX : ViewModel() {
    private val bus = VMBus()

    fun connect(owner: LifecycleOwner, block: Bus.() -> Unit) {
        bus.with(owner).block()
    }

    fun postUI(flag: Int, any: Any? = 0) = bus.post(flag, any)

    fun sendUI(flag: Int, any: Any? = 0) = bus.send(flag, any)

    fun onStart() {}
}