package lava.core.design.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import lava.core.bus.Flag
import lava.core.bus.LiveBus
import lava.core.bus.VMBus
import lava.core.design.view.struct.StructState
import lava.core.ext.just
import lava.core.obj.UNCHECKED_CAST
import lava.core.widget.list.page.IVMPlugin

abstract class ViewModelX : ViewModel() {
    private val bucket by lazy { mutableMapOf<String, Any>() }
    private val structStateMap by lazy { mutableMapOf<StructState<*>, MutableList<IVMPlugin>>() }

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
        val exist = bucket[key] as? T
        if (exist != null) {
            return exist
        }
        val obj = block()
        bucket[key] = obj
        return obj
    }

    @Synchronized
    internal fun <T : Any> get(key: String): T? {
        @Suppress(UNCHECKED_CAST)
        return bucket[key] as? T
    }

    fun <T> registerPlugin(state: StructState<T>, plugin: IVMPlugin) {
        var list = structStateMap[state]
        if (list == null) {
            list = mutableListOf()
            structStateMap[state] = list
        }
        list.add(plugin)
    }

    fun <T> onViewStateChanged(state: StructState<T>): T {
        @Suppress(UNCHECKED_CAST)
        structStateMap[state]?.just {
            if (state.default == Unit) {
                for (plugin in this) {
                    plugin.onViewStateChange(state)
                }
            } else {
                val plugin = firstOrNull { it.handled() }
                if (plugin != null) {
                    return plugin.onViewStateChange(state) as T
                }
            }
        }

        return state.default
    }

    open fun onInit() {}

    open fun onStart() {}

    /**
     * return can return or not
     */
    open fun onBackPressed(): Boolean {
        return true
    }
}