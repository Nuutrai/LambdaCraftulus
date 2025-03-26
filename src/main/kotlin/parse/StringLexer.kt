package me.chriss99.parse

import java.util.LinkedList

fun lex(source: String) : LinkedList<Token> {
    return LinkedList(source.map { char ->
        when (char) {
            '\\' -> Token.Lambda
            '.' -> Token.Dot
            '(' -> Token.LParen
            ')' -> Token.RParen
            else -> if (char.isLetter()) Token.Var(char.toString()) else throw IllegalArgumentException()
        }
    })
}