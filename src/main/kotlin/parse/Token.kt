package me.chriss99.parse

sealed class Token {
    class Var(val name: String) : Token()
    data object Lambda : Token()
    data object Dot : Token()
    data object LParen : Token()
    data object RParen : Token()
}