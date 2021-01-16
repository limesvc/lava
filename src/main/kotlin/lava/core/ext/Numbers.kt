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
