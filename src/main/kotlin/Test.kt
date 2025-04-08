package me.chriss99

import me.chriss99.parse.ParsingException
import me.chriss99.parse.lex
import me.chriss99.parse.parse

fun main() {
    println( try {
        parse(lex("((\\x.(\\y.(y((xx)y))))\\z.(\\w.(w((zz)w))))\\a.(\\b.(((((\\c.(\\d.(\\e.(c(\\f.e)d))))b)\\g.(\\h.(g(h))))((\\i.(\\j.(\\k.(j(i(k))))))a)(a(\\l.(\\m.(\\n.((((l)(\\p.(\\q.(q(p(m))))))(\\r.(n)))(\\s.(s))))))))b)))(\\t.(\\u.(t(t(t(u)))))))"))
    } catch (e: ParsingException) {
        e.problem
    }
    )
}