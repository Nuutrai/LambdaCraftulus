package me.chriss99.lambdaCraftulus

import me.chriss99.lambdaCraftulus.Expression.*

fun reduce(appl: Apply) : Expression {
    return when (appl.apply) {
        is Var, is Apply -> appl
        is Lambda -> replace(appl.apply.body, appl.apply.variable, appl.to)
    }
}

private fun replace(expr: Expression, replace: Var, with: Expression) : Expression {
    return when (expr) {
        is Var -> if (expr == replace) with else expr
        is Lambda -> Lambda(expr.variable, replace(expr.body, replace, with))
        is Apply -> Apply(replace(expr.apply, replace, with), replace(expr.to, replace, with))
    }
}

fun reduceAll(expr: Expression, tech: (expr: Expression) -> Apply?) : Expression {
    var current = expr
    while (true) {
        val reducible = tech(expr) ?: break
        current = reduce(reducible)
    }
    return current
}