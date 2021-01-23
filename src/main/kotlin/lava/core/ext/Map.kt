package lava.core.ext

fun <T, V> MutableMap<T, V>.putIfNull(key: T, value: V){
    val v = get(key)
    if (v == null) {
        put(key, value)
    }
}