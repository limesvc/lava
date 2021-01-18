package lava.core.design.view.struct

import lava.core.obj.UNCHECKED_CAST

abstract class DecorStructX : DecorStruct {

    override fun <T> get(struct: Struct<T>): T? {
        @Suppress(UNCHECKED_CAST)
        return if (DecorStruct == struct) this as T else null
    }

    override fun install(struct: StructView, host: StructHost) {
        host.onSetup(host.binding())
    }
}