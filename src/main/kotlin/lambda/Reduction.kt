package me.chriss99.lambda

import me.chriss99.lambda.Expression.*

fun reduce(appl: Apply) : Expression {
    return when (appl.apply) {
        is Var, is Apply -> appl
        is Lambda -> replace(appl.apply.body, appl.apply.variable, appl.to)
    }
}

private fun replace(expr: Expression, replace: Var, with: Expression) : Expression {
    return when (expr) {
        is Var -> if (expr.name == replace.name) with else expr
        is Lambda -> Lambda(expr.variable, replace(expr.body, replace, with))
        is Apply -> Apply(replace(expr.apply, replace, with), replace(expr.to, replace, with))
    }
}

private fun reduceAt(expr: Expression, appl: Apply): Expression {
    if (expr === appl)
        return reduce(expr)

    return when (expr) {
        is Var -> expr
        is Lambda -> Lambda(expr.variable, reduceAt(expr.body, appl))
        is Apply -> Apply(reduceAt(expr.apply, appl), reduceAt(expr.to, appl))
    }
}

fun reduceAll(expr: Expression, strategy: (expr: Expression) -> Apply?) : Expression {
    var current = expr
    while (true) {
        val reducible = strategy(current) ?: break
        current = reduceAt(current, reducible)
    }
    return current
}