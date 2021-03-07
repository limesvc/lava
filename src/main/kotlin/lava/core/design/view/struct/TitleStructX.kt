package lava.core.design.view.struct

import lava.core.obj.UNCHECKED_CAST

abstract class TitleStructX : TitleStruct {

    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (TitleStruct == struct) this as T else null
    }
}