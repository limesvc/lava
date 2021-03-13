package lava.core.design.viewmodel

import kotlin.coroutines.cancellation.CancellationException

/**
 * Created by svc on 2021/3/13
 */
class LoadCancelException(msg: String) : CancellationException(msg)