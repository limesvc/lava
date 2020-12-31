package lava.core.ext

import android.content.SharedPreferences
import lava.core.obj.toJson

/**
 * Created by svc on 2020/12/30
 */
fun SharedPreferences.applyValue(key: String, value: Any) {
    edit().also {
        when (value) {
            is Boolean -> it.putBoolean(key, value)
            is Int -> it.putInt(key, value)
            is Long -> it.putLong(key, value)
            is Float -> it.putFloat(key, value)
            is String -> it.putString(key, value)
            else -> it.putString(key, value.toJson())
        }
    }.apply()
}