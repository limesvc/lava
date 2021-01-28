package lava.core.widget.list.page

data class ParseResult<T>(val success: Boolean, val list: List<T>? = null)