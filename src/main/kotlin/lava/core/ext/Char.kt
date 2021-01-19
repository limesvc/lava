package lava.core.ext

fun Char.isDigit():Boolean{
    val ascii = this.toInt()
    return ascii in 48..57
}