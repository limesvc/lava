package lava.core.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import lava.core.ext.am

/**
 * Created by svc on 2021/2/23
 */

internal class Box {
    var next: Box? = null
    fun onEvent(any: Any?) {}
}

class EventFlow {
    private fun test() {
        val channel = Channel<Event>()
        GlobalScope.launch {
            channel.consumeEach {

            }
        }
    }

    private val live = MutableLiveData<Event>()

    fun with(owner: LifecycleOwner): Bus {
        return Bus(owner, live)
    }

    /**
     * async in mainThread
     */
    fun post(flag: Int, any: Any? = null) {
        live.postValue(Event.obtain(flag, any))
    }

    /**
     * sync in any thread
     */
    fun send(flag: Int, any: Any? = null) {
        live.value = Event.obtain(flag, any)
    }

    /**
     * async in mainThread
     */
    fun <T> post(flag: Flag<T>, any: T? = null) {
        live.postValue(Event.obtain(flag.flag, any))
    }

    /**
     * sync in any thread
     */
    fun <T> send(flag: Flag<T>, any: T? = null) {
        live.value = Event.obtain(flag.flag, any)
    }
}

class FLow(private val owner: LifecycleOwner, private val live: MutableLiveData<Event>) :
    MutableLiveData<Event>() {

    inline fun <reified T> on(flag: Flag<T>, crossinline func: Function1<T, Unit>) {
        onAny(flag.flag) { any ->
            any.am<T> {
                func(it)
            }
        }
    }

    inline fun <reified T : Any> on(flag: Int, crossinline func: Function1<T, Unit>): FLow {
        onAny(flag) { any ->
            any.am<T> {
                func(it)
            }
        }
        return this
    }

    fun on(flag: Int, func: Function0<Unit>): FLow {
        live.observe(owner) {
            if (it.flag == flag) {
                func()
            }
            it.recycle()
        }
        return this
    }

    /**
     * 除非确定要做一个可变类型参数，否则不要使用这个方法
     */
    fun onAny(flag: Int, func: Function1<Any?, Unit>): FLow {
        live.observe(owner) {
            if (it.flag == flag) {
                func(it.any)
            }
            it.recycle()
        }
        return this
    }
}