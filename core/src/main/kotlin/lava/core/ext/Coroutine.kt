package lava.core.ext

import android.os.SystemClock
import kotlinx.coroutines.*
import lava.core.type.Block

// should be named as `delay`, but coroutine toke it
fun after(millis: Long, block: Block): Job {
    return GlobalScope.launch {
        delay(millis)
        block()
    }
}

fun afterOnUI(millis: Long, block: Block): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        delay(millis)
        block()
    }
}

fun interval(interval: Long, block: Block): Job {
    return GlobalScope.launch {
        var start = SystemClock.elapsedRealtime()
        var last = start
        var nextTick = 0L
        var offset = 0L
        var now = 0L
        while (true) {
            now = SystemClock.elapsedRealtime()
            offset = now - last
            nextTick = interval - offset % interval
            last = now

            block()
            delay(nextTick)
        }
    }
}

fun CoroutineScope.interval(millis: Long, block: Block): Job {
    return launch {
        while (true) {
            block()
            delay(millis)
        }
    }
}

fun CoroutineScope.launchIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.IO, start, block)

fun CoroutineScope.launchMain(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Main, start, block)