package me.chriss99.codespace

import me.chriss99.minestom.BlockSymbol.*
import me.chriss99.minestom.LambdaSymbolManager
import me.chriss99.minestom.fromMaterial
import minestom.BASE_BLOCK
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag

class CodeSpace(val instance: Instance, val expressions: HashMap<Int, MutableList<Expression>>, val macros: HashMap<Block, Expression>) {

    fun addBlock(at: Point, item: ItemStack) {

        val block = fromMaterial(item.material())?.block ?: item.material().block() ?: return
        var expression: Expression =
            if (block.compare(DEFINE.block)) addExpression(at)
            else getExpression(at) ?: addExpression(at)

//        expression.addBlock

        // FIXME Ignore everything below here, these are artifacts of when I copied something

        val symbol = LambdaSymbolManager.createLambdaSymbol(block, at, instance)
        val lambdaBlock = block.withTag(Tag.UUID("link"), symbol)

        instance.setBlock(at, lambdaBlock)
    }

    fun addExpression(at: Point): Expression {

        val exprsAtY = expressions[at.blockY()] ?: mutableListOf<Expression>().also { expressions[at.blockY()] = it }
        val block = instance.getBlock(at.add(1.0, 0.0, 0.0))
        val expression = Expression(this,
            at,
            if (block.compare(BASE_BLOCK))
                null
            else block
        )

        exprsAtY.add(expression)

        return expression

    }

    private fun getExpression(from: Point): Expression? {

        var expression: Expression? = null

        for (expr in expressions[from.blockY()] ?: return null) {
            if (expr.source.blockX() < from.blockX()) continue
            if (expr.source.blockX() < (expression?.source?.blockX() ?: Int.MAX_VALUE )) {
                expression = expr
            }
        }

        return expression
    }

}