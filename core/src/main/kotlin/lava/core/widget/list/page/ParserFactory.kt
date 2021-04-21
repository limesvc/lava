package lava.core.widget.list.page

import lava.core.obj.UNCHECKED_CAST
import kotlin.reflect.KClass

typealias Parser<T> = (T) -> ParseResult<*>

object ParserFactory {
    private var parserMap = mutableMapOf<KClass<out Any>, Parser<Any>>()

    init {
        addParser(List::class) {
            ParseResult(true, it)
        }
    }

    fun <T: Any> addParser(clazz: KClass<T>, parser: Parser<T>) {
        @Suppress(UNCHECKED_CAST)
        parserMap[clazz] = parser as Parser<Any>
    }

    fun <DATA> parse(input: Any): ParseResult<DATA>? {
        for ((clazz, parser) in parserMap) {
            if (clazz.isInstance(input)) {
                @Suppress(UNCHECKED_CAST)
                return parser(input) as? ParseResult<DATA>
            }
        }
        return null
    }
}