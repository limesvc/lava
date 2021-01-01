package lava.core.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import lava.core.ext.am

/**
 * example:
 * object Bus: LiveBus(){
 *      const val EVENT_X = 0
 * }
 */
abstract class LiveBus {
    private val live = MutableLiveData<Event>()

    fun with(owner: LifecycleOwner): Live {
        return Live(owner, live)
    }
}

class Live(private val owner: LifecycleOwner, private val live: MutableLiveData<Event>) {

    inline fun <reified T : Any> on(flag: Int, crossinline func: Function1<T, Unit>): Live {
        onAny(flag) { any ->
            any.am<T> {
                func(it)
            }
        }
        return this
    }

    fun on(flag: Int, func: Function0<Unit>): Live {
        live.observe(owner, Observer {
            if (it.flag == flag) {
                func()
            }
        })
        return this
    }

    /**
     * 除非确定要做一个可变类型参数，否则不要使用这个方法
     */
    fun onAny(flag: Int, func: Function1<Any, Unit>) {
        live.observe(owner, Observer {
            if (it.flag == flag) {
                func(it.any)
            }
        })
    }

    fun post(flag: Int, any: Any? = null) {
        live.postValue(Event(flag, any ?: Unit))
    }
}

data class Event(val flag: Int, val any: Any = Unit)