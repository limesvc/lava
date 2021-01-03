package lava.core.ext

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

fun interval(millis: Long, block: Block): Job {
    return GlobalScope.launch {
        while (true) {
            delay(millis)
            block()
        }
    }
}