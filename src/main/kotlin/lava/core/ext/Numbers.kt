@file:Suppress("NOTHING_TO_INLINE")

package lava.core.ext

inline fun Short?.or0(): Short {
    return this ?: 0
}

inline fun Int?.or0(): Int {
    return this ?: 0
}

inline fun Long?.or0(): Long {
    return this ?: 0L
}

inline fun Float?.or0(): Float {
    return this ?: 0f
}

inline fun Double?.or0(): Double {
    return this ?: 0.0
}

inline fun String?.safeInt(default: Int = 0): Int {
    return if (this == null) default else try {
        Integer.parseInt(this)
    } catch (e: Throwable) {
        default
    }
}

inline fun String?.safeShort(default: Short = 0): Short {
    return if (this == null) default else try {
        this.toShort()
    } catch (e: Throwable) {
        default
    }
}

inline fun String?.safeLong(default: Long = 0): Long {
    return if (this == null) default else try {
        this.toLong()
    } catch (e: Throwable) {
        default
    }
}

inline fun String?.safeFloat(default: Float = 0f): Float {
    return if (this == null) default else try {
        this.toFloat()
    } catch (e: Throwable) {
        default
    }
}

inline fun String?.safeDouble(default: Double = 0.0): Double {
    return if (this == null) default else try {
        this.toDouble()
    } catch (e: Throwable) {
        default
    }
}

fun <T : Number, R> doOnNotZero(t: T?, block: (T) -> R?): R? {
    return if (t != null && t != 0) {
        block.invoke(t)
    } else {
        null
    }
}

fun <T1 : Number, T2 : Number, R> doOnNotZero(t1: T1?, t2: T2?, block: (t1: T1, t2: T2) -> R?): R? {
    return if (notNull(t1, t2)) {
        if (notZero(t1!!, t2!!)) {
            block.invoke(t1, t2)
        } else {
            null
        }
    } else null
}

private fun <T : Number> notZero(vararg ts: T): Boolean {
    ts.forEach {
        if (it == 0) return false
    }
    return true
}