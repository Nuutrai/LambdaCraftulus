package me.chriss99.parse

fun lex(source: String) : List<Token> {
    return source.map { char ->
        when (char) {
            '\\' -> Token.Lambda
            '.' -> Token.Dot
            '(' -> Token.LParen
            ')' -> Token.RParen
            else -> if (char.isLetter()) Token.Var(char.toString()) else throw IllegalArgumentException()
        }
    }
}