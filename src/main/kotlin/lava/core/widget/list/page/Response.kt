package lava.core.widget.list.page

data class Response<T>(val success: Boolean, val list: List<T>? = null)