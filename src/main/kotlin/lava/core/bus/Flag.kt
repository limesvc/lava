package lava.core.bus

/**
 * Created by svc on 2021/1/20
 */
open class Flag<T>(val flag: Int, var value: T? = null)

class FlagInt(flag: Int) : Flag<Int>(flag) {}
class FlagLong(flag: Int) : Flag<Int>(flag) {}
class FlagBool(flag: Int) : Flag<Int>(flag) {}
class FlagString(flag: Int) : Flag<Int>(flag) {}