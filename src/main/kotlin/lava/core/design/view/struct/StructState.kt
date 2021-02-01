package lava.core.design.view.struct

/**
 * Created by svc on 2021/1/29
 */
sealed class StructState<T>(val default: T)

object OnCreate : StructState<String>("")

object OnLoaded : StructState<String>("")

object OnRetry : StructState<String>("")

object OnBackPressed : StructState<Boolean>(false)