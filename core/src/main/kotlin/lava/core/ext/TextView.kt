package lava.core.ext

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView

/**
 * Created by svc on 2021/2/3
 */

fun TextView?.bold() {
    this?.typeface = Typeface.defaultFromStyle(Typeface.BOLD);
}

fun TextView?.mediumBold() {
    this?.paint?.isFakeBoldText = true
}

fun TextView?.cancelBold() {
    this?.typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
}

fun TextView?.underline() {
    this?.just {
        paintFlags = paintFlags.or(Paint.UNDERLINE_TEXT_FLAG)
    }
}

fun TextView?.strikeThrough() {
    this?.just {
        paintFlags = paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
    }
}