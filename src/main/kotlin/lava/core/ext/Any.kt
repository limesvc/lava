package lava.core.ext

inline fun <reified T> Any?.am(block: Function1<T, Unit>) {
    if (this != null && this is T) {
        block(this)
    }
}

inline fun <T, R> notNull(t: T?, r: R?, block: Function2<T, R, Unit>) {
    if (t != null && r != null) block(t, r)
}

inline fun <T, R, V> notNull(t: T?, r: R?, v: V?, block: Function3<T, R, V, Unit>) {
    if (t != null && r != null && v != null) block(t, r, v)
}