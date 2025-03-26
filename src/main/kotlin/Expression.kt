package me.chriss99.lambdaCraftulus

import java.util.UUID

sealed class Expression {
    class Var(val name: String, val uuid: UUID) : Expression()
    class Lambda(val variable: Var, val body: Expression) : Expression()
    class Apply(val apply: Expression, val to: Expression) : Expression()

    override fun toString(): String {
        return when (this) {
            is Var -> name
            is Lambda -> "\\$variable.$body"
            is Apply -> "(${if (apply !is Apply) "($apply)" else apply.toString()}$to)"
        }
    }
}