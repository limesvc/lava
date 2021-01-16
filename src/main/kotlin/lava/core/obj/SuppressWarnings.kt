package lava.core.obj

/**
 * 提供合适的警告压制类型，主要用于Java
 * https://blog.csdn.net/u012994320/article/details/83083392
 */

const val all = "all" // to suppress all warnings（抑制所有警告）

const val boxing = "boxing" // to suppress warnings relative to boxing/unboxing operations（要抑制与箱/非装箱操作相关的警告）

const val cast = "cast" // to suppress warnings relative to cast operations（为了抑制与强制转换操作相关的警告）

const val depAnn = "dep-ann" // to suppress warnings relative to deprecated annotation（要抑制相对于弃用注释的警告）

const val deprecation = "deprecation" // to suppress warnings relative to deprecation（要抑制相对于弃用的警告）

const val fallthrough = "fallthrough" // to suppress warnings relative to missing breaks in switch statements（在switch语句中，抑制与缺失中断相关的警告）

const val finally = "finally" //to suppress warnings relative to finally block that don’t return（为了抑制警告，相对于最终阻止不返回的警告）

const val hiding = "hiding" // to suppress warnings relative to locals that hide variable（为了抑制本地隐藏变量的警告）

const val incompleteSwitch = "incomplete-switch" // to suppress warnings relative to missing entries in a switch statement (enum case)（为了在switch语句（enum案例）中抑制相对于缺失条目的警告）

const val nls = "nls" // to suppress warnings relative to non-nls string literals（要抑制相对于非nls字符串字面量的警告）

const val NULL = "null" // to suppress warnings relative to null analysis（为了抑制与null分析相关的警告）

const val rawTypes = "rawtypes" // to suppress warnings relative to un-specific types when using generics on class params（在类params上使用泛型时，要抑制相对于非特异性类型的警告）

const val restriction = "restriction" // to suppress warnings relative to usage of discouraged or forbidden references（禁止使用警告或禁止引用的警告）

const val serial = "serial" // to suppress warnings relative to missing serialVersionUID field for a serializable class（为了一个可串行化的类，为了抑制相对于缺失的serialVersionUID字段的警告）

const val staticAccess = "static-access" // o suppress warnings relative to incorrect static access（o抑制与不正确的静态访问相关的警告）

const val syntheticAccess = "synthetic-access" // to suppress warnings relative to unoptimized access from inner classes（相对于内部类的未优化访问，来抑制警告）

const val unchecked = "unchecked" // to suppress warnings relative to unchecked operations（相对于不受约束的操作，抑制警告）

const val unqualifiedFieldAccess = "unqualified-field-access" // to suppress warnings relative to field access unqualified（为了抑制与现场访问相关的警告）

const val unused = "unused" // to suppress warnings relative to unused code（抑制没有使用过代码的警告）