package me.chriss99.lambdaCraftulus

import me.chriss99.lambdaCraftulus.Expression.*
import java.util.*

fun lazyReducible(expr: Expression) : Apply? {
    val queue: LinkedList<Expression> = LinkedList()
    queue.add(expr)

    while (!queue.isEmpty())
        when (val current = queue.pop()) {
            is Var -> {}
            is Lambda -> queue.add(current.body)
            is Apply -> if (current.apply is Lambda)
                return current
            else {
                queue.add(current.apply)
                queue.add(current.to)
            }
        }

    return null
}

fun lazyReduce(expr: Expression) : Expression {
    return reduceAll(expr, ::lazyReducible)
}