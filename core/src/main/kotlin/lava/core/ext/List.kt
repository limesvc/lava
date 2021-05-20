package lava.core.ext

/**
 * Created by svc on 2021/5/20
 */
fun <M, K, V> List<M>.toMap(map: (M) -> Pair<K, V>): Map<K, V> {
    val result = mutableMapOf<K, V>()
    this.forEach {
        val pair = map(it)
        result[pair.first] = pair.second
    }
    return result
}