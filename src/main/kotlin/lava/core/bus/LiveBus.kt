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

    fun with(owner: LifecycleOwner): Bus {
        return Bus(owner, live)
    }

    /**
     * async in mainThread
     */
    fun post(flag: Int, any: Any? = null) {
        live.postValue(Event(flag, any))
    }

    /**
     * sync in any thread
     */
    fun send(flag: Int, any: Any? = null) {
        live.value = Event(flag, any)
    }
}

class Bus(private val owner: LifecycleOwner, private val live: MutableLiveData<Event>) :
    MutableLiveData<Event>() {

    inline fun <reified T : Any> on(flag: Int, crossinline func: Function1<T, Unit>): Bus {
        onAny(flag) { any ->
            any.am<T> {
                func(it)
            }
        }
        return this
    }

    fun on(flag: Int, func: Function0<Unit>): Bus {
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
    fun onAny(flag: Int, func: Function1<Any?, Unit>): Bus {
        live.observe(owner, Observer {
            if (it.flag == flag) {
                func(it.any)
            }
        })
        return this
    }
}

data class Event(val flag: Int, val any: Any? = null)