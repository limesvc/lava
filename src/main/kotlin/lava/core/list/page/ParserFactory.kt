package lava.core.list.page

import lava.core.obj.UNCHECKED_CAST
import kotlin.reflect.KClass

typealias Parser<T> = (Any) -> Response<T>

object ParserFactory {
    private var parserMap = mutableMapOf<KClass<*>, Parser<*>>()

    init {
        addParser(List::class) {
            if (it is List<*>) Response(true, it) else Response(false)
        }
    }

    fun <T> addParser(clazz: KClass<*>, parser: Parser<T>) {
        parserMap[clazz] = parser
    }

    fun <DATA> parse(input: Any): Response<DATA>? {
        for ((clazz, parser) in parserMap) {
            if (clazz.isInstance(input)) {
                @Suppress(UNCHECKED_CAST)
                return parser(input) as? Response<DATA>
            }
        }
        return null
    }
}