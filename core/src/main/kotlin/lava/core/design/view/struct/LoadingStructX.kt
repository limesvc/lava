package lava.core.design.view.struct

import lava.core.obj.UNCHECKED_CAST

abstract class LoadingStructX : LoadingStruct {
    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (LoadingStruct == struct) this as T else null
    }
}