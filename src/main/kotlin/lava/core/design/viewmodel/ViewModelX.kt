package lava.core.design.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lava.core.bus.Flag
import lava.core.bus.LiveBus
import lava.core.bus.VMBus
import lava.core.design.view.struct.OnBackPressed
import lava.core.design.view.struct.StructState
import lava.core.ext.just
import lava.core.ext.launchMain
import lava.core.net.LoadingState
import lava.core.obj.UNCHECKED_CAST
import lava.core.widget.list.page.IVMPlugin
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class ViewModelX : ViewModel() {
    private val bucket by lazy { mutableMapOf<String, Any>() }
    private val structStateMap by lazy { mutableMapOf<StructState<*>, MutableList<IVMPlugin>>() }

    private val bus = VMBus()

    private val loadingStack by lazy { Stack<Job>() }

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
        structStateMap[state]?.just {
            if (state.default == Unit) {
                for (plugin in this) {
                    plugin.onViewStateChange(state)
                }
            } else {
                val plugin = firstOrNull { it.handled() }
                if (plugin != null) {
                    @Suppress(UNCHECKED_CAST)
                    return plugin.onViewStateChange(state) as T
                }
            }
        }

        if (state == OnBackPressed) {
            @Suppress(UNCHECKED_CAST)
            return onBackPressed() as T
        }

        return state.default
    }

    open fun onInit() {}

    open fun onStart() {}

    /**
     * return can return or not
     */
    open fun onBackPressed(): Boolean {
        if (loadingStack.isNotEmpty()) {
            loadingStack.pop()?.cancel(LoadCancelException(""))
            return false
        }
        return true
    }

    protected fun load(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onCancelState: LoadingState = LoadingState.READY,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        asLoading()
        val task = viewModelScope.launch(context, start) {
            kotlin.runCatching {
                this.block()
                withContext(Dispatchers.Main) { asLoaded() }
            }.onFailure {
                it.printStackTrace()
                withContext(Dispatchers.Main) { asError() }
            }
        }
        task.invokeOnCompletion {
            viewModelScope.launchMain {
                if (it is LoadCancelException) {
                    onState(onCancelState)
                } else {
                    it?.printStackTrace()
                }
            }
        }
        loadingStack.add(task)
        return task
    }
}