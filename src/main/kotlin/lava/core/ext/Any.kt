package lava.core.ext

inline fun <reified T> Any?.am(block: Function1<T, Unit>) {
    if (this != null && this is T) {
        block(this)
    }
}

inline fun <T: Any> T?.just(block: T.() -> Unit){
    this?.block()
}

inline fun <T, R> onNotNull(t: T?, r: R?, block: Function2<T, R, Unit>) {
    if (t != null && r != null) block(t, r)
}

inline fun <T, R, V> onNotNull(t: T?, r: R?, v: V?, block: Function3<T, R, V, Unit>) {
    if (t != null && r != null && v != null) block(t, r, v)
}

internal fun notNull(vararg ts: Any?): Boolean {
    ts.forEach {
        if (it == null) return false
    }
    return true
}