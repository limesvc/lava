@file:Suppress(UNUSED)

package lava.core.obj

/**
 * 提供合适的警告压制类型，主要用于Kotlin
 * https://droidyue.com/blog/2019/08/03/kotlinc-config-warnings-as-errors/
 */

const val NOTHING_TO_INLINE = "NOTHING_TO_INLINE" // 作用不大的内联

const val INACCESSIBLE_TYPE = "INACCESSIBLE_TYPE" // 不可访问的类型

const val UNCHECKED_CAST = "UNCHECKED_CAST" // 未检查的类型转换

const val WHEN_ENUM_CAN_BE_NULL_IN_JAVA = "WHEN_ENUM_CAN_BE_NULL_IN_JAVA" // Enum 可能为null

const val PARAMETER_NAME_CHANGED_ON_OVERRIDE = "PARAMETER_NAME_CHANGED_ON_OVERRIDE" // 方法重写修改参数名

const val NAME_SHADOWING = "NAME_SHADOWING" // 命名遮挡

const val UNNECESSARY_SAFE_CALL = "UNNECESSARY_SAFE_CALL" // 不必要的安全调用

const val SENSELESS_COMPARISON = "SENSELESS_COMPARISON" // 无意义的比较

const val UNNECESSARY_NOT_NULL_ASSERTION = "UNNECESSARY_NOT_NULL_ASSERTION" // 不需要的非空断言

const val USELESS_IS_CHECK = "USELESS_IS_CHECK" // 没有用的实例类型检查

const val VARIABLE_WITH_REDUNDANT_INITIALIZER = "VARIABLE_WITH_REDUNDANT_INITIALIZER" // 变量初始化多余

const val DEPRECATION = "DEPRECATION" // 方法弃用

const val UNUSED = "UNUSED" // 任何未使用

const val UNUSED_PARAMETER = "UNUSED_PARAMETER" // 参数没有使用

const val UNUSED_VARIABLE = "UNUSED_VARIABLE" // 变量没有使用

const val UNUSED_VALUE = "UNUSED_VALUE" // 未使用的值

const val ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE = "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE" // 赋值后未使用的变量

const val NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS = "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS" // 使用可空的Java泛型