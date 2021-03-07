package lava.core.design.view.struct

/**
 * Created by svc on 2021/1/29
 */
sealed class StructState<T>(val default: T)

object OnCreate : StructState<Unit>(Unit)

object OnLoaded : StructState<Unit>(Unit)

object OnRetry : StructState<Unit>(Unit)

object OnBackPressed : StructState<Boolean>(true)