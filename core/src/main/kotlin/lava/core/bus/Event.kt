package lava.core.bus

import logger.L

class Event private constructor(var flag: Int, var any: Any? = null) {
    companion object {
        private const val MAX_POOL_SIZE = 15
        private var pool: Event? = null
        private var poolSize = 0

        @Synchronized
        internal fun obtain(flag: Int, any: Any? = null): Event {
            val event = pool
//            if (event != null) {
//                pool = event.next
//                event.any = any
//                event.flag = flag
//                poolSize--
//                return event
//            }
            L.d("Event pool out of cache")
            return Event(flag, any)
        }
    }

    private var next: Event? = null

    internal fun recycle() {
        if (poolSize < MAX_POOL_SIZE) {
            any = null
            next = pool
            poolSize++
            pool = this
        }
    }
}