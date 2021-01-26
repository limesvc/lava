package lava.core.ext

/**
 * Created by svc on 2021/1/26
 */

fun String?.isNotEmpty():Boolean{
    return this != null && this != ""
}