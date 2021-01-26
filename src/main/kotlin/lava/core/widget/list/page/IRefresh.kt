package lava.core.widget.list.page

import lava.core.net.LoadingState
import lava.core.type.Block

/**
 * Created by wuxi on 2021/1/26
 */
interface IRefresh {
    fun onRefresh(block: Block)

    fun updateState(state: LoadingState)
}

interface ILoadMore {
    fun onLoadMore(block: Block)

    fun updateState(state: LoadingState)
}

interface ILoadLayer {
    fun onLoad(block: Block)

    fun updateState(state: LoadingState)
}