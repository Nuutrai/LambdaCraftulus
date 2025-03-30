package me.chriss99.parse

import me.chriss99.lambda.Expression
import java.util.HashMap
import java.util.UUID
import kotlin.reflect.KClass

fun parse(tokens: List<Token>): Expression {
    return parse(listOf(*tokens.toTypedArray(), Token.EndOfFile), 0, HashMap()).first
}

fun parse(tokens: List<Token>, i: Int, ids: HashMap<String, UUID>): Pair<Expression, Int> {
    when (val current = tokens[i]) {
        is Token.Var -> return Expression.Var(current.name, idOf(current.name, ids)) to i
        is Token.Lambda -> {
            val variable = verifyToken<Token.Var>(tokens, i+1, i).name.let { Expression.Var(it, newID(it, ids, tokens, i+1)) }
            verifyToken<Token.Dot>(tokens, i+2, i, i+1)
            val (body, i) = parse(tokens, i+3, ids)

            return Expression.Lambda(variable, body) to i
        }
        is Token.LParen -> {
            var (expr, exprEnd) = parse(tokens, i+1, ids)

            while (tokens[exprEnd+1] !is Token.RParen) {
                val res = parse(tokens, exprEnd+1, ids)
                expr = Expression.Apply(expr, res.first)
                exprEnd = res.second
            }

            return expr to exprEnd+1
        }
        else -> throw ParsingException.NoMatchingPatternException(tokens, i, Token.Var::class, Token.Lambda::class, Token.LParen::class)
    }
}

private fun newID(name: String, ids: HashMap<String, UUID>, tokens: List<Token>, index: Int): UUID {
    if (ids.containsKey(name))
        throw ParsingException.NonUniqueVariableException(tokens, name, index)
    val id = UUID.randomUUID()
    ids[name] = id
    return id
}

fun <T> idOf(name: T, ids: HashMap<T, UUID>): UUID {
    return ids[name] ?: UUID.randomUUID().also { ids[name] = it }
}

private inline fun <reified T : Token> verifyToken(tokens: List<Token>, index: Int, vararg indexes: Int): T {
    val token = tokens[index]
    if (token !is T)
        throw ParsingException.UnexpectedTokenException(tokens, T::class, token::class, *indexes, index)
    return token
}

sealed class ParsingException(val tokens: List<Token>, vararg val indexes: Int, message: String) : Exception(message) {
    override fun toString(): String {
        var message = "${this::class.simpleName}: $message" + System.lineSeparator()
        message += tokens.fold("") { a, b -> a + tokenToString(b) }

        val marker = List(tokens.size) { " " }.toMutableList()
        for (i in indexes.first()..indexes.last())
            marker[i] = "-"
        for (i in indexes)
            marker[i] = "+"
        marker[indexes.last()] = "^"


        return message + System.lineSeparator() + marker.fold("") { a, b -> a + b }
    }

    class UnexpectedTokenException(tokens: List<Token>, expected: KClass<out Token>, found: KClass<out Token>, vararg indexes: Int) : ParsingException(tokens, *indexes,
        message = "Expected ${expected.simpleName} but found ${found.simpleName}.")
    class NoMatchingPatternException(tokens: List<Token>, index: Int, vararg expected: KClass<out Token>) : ParsingException(tokens, index,
        message = "Expected one of ${expected.map { it.simpleName }} but found ${tokens[index]::class.simpleName}.")
    class NonUniqueVariableException(tokens: List<Token>, name: String, index: Int) : ParsingException(tokens, index,
        message = "\"$name\" is already bound, but found Lambda binding it as its variable!")
}