package lava.core.design.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lava.core.bus.*
import lava.core.design.view.struct.*
import lava.core.obj.UNCHECKED_CAST

abstract class ViewModelX : ViewModel() {
    private val bin by lazy { mutableMapOf<String, Any>() }

    private val bus = VMBus()

    protected infix fun <T> Flag<T>.set(value: T) {
        viewModelScope
        this.value = value
        bus.send(flag, value)
    }

    protected operator fun <T> Flag<T>.invoke(value: T? = null) {
        this.value = value
        bus.send(flag, value)
    }

    fun connect(block: LiveBus.() -> Unit) {
        bus.block()
    }

    fun postUI(flag: Int, any: Any? = 0) = bus.post(flag, any)

    fun sendUI(flag: Int, any: Any? = 0) = bus.send(flag, any)

    @Synchronized
    internal fun <T : Any> getOrCreate(key: String, block: () -> T): T {
        @Suppress(UNCHECKED_CAST)
        val exist = bin[key] as? T
        if (exist != null) {
            return exist
        }
        val obj = block()
        bin[key] = obj
        return obj
    }

    fun <T> onViewStateChanged(state: StructState<T>): T {
        when (state) {
            is OnCreate -> Unit
            is OnLoaded -> onStart()
            is OnRetry -> onRetry()
            is OnBackPressed -> Unit
        }

        return state.default
    }

    open fun onStart() {}

    open fun onRetry() {}

    /**
     * return intercept or not
     */
    open fun onBackPressed(): Boolean {
        return false
    }
}