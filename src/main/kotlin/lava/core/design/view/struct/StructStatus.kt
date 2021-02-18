package lava.core.design.view.struct

import lava.core.net.LoadingState

/**
 * Created by svc on 2021/2/15
 */
data class StructStatus<T> (val struct: Struct<T>, val state: LoadingState)