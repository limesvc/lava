package lava.core.ext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by svc on 2021/1/26
 */

fun String?.isNotEmpty():Boolean{
    return this != null && this != ""
}

fun String.matcher(regex: String): Matcher {
    return Pattern.compile("[0-9]+").matcher(this)
}