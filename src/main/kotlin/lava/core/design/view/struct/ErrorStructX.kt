package lava.core.design.view.struct

import lava.core.obj.UNCHECKED_CAST

class ErrorStructX : ErrorStruct {

    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (ErrorStruct == struct) this as T else null
    }
}