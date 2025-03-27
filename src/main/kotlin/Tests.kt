package me.chriss99

import me.chriss99.lambda.lazyReduce
import me.chriss99.parse.lex
import me.chriss99.parse.parse

fun main() {
//    println(lazyReduce(parse(
//            lex("((\\a.(\\b.(b((aa)b))))(\\a.(\\b.(b((aa)b)))))"),
//        ),),)
    println(
        lazyReduce(
            parse(
        lex("(((\\a.\\b.(ba))(\\x.\\y.(x(x(xy)))))(\\w.\\z.(w(w(w(wz))))))"),
    )
        ,)
    ,)
}