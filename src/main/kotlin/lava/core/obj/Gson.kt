package lava.core.obj

import com.google.gson.Gson
import kotlin.reflect.KClass

/**
 * Created by wuxi on 2020/12/30
 */
val gson = Gson()

fun Any.toJson(): String {
    return gson.toJson(this)
}

fun <T> Class<T>.fromJson(json: String): T {
    return gson.fromJson(json, this)
}

fun <T: Any> KClass<T>.fromJson(json: String): T {
    return gson.fromJson(json, this.java) as T
}