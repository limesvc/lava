package lava.core.design.view.struct

import lava.core.obj.UNCHECKED_CAST

class LoadingStructX : LoadingStruct {
    companion object Key : Struct<LoadingStruct>

    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (Key == struct) this as T else null
    }
}