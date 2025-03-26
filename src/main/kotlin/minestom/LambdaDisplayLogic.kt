package me.chriss99.minestom

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.BlockVec
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.metadata.display.TextDisplayMeta
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.tag.Tag
import java.util.*

fun lambdaDisplayLogic(eventHandler: GlobalEventHandler) {
    eventHandler.addListener(PlayerBlockPlaceEvent::class.java) { event ->
        event.isCancelled = true
        val instance = event.instance
        val below = event.blockPosition.sub(0, 1, 0)
        if (instance.getBlock(below) != Block.BLACK_STAINED_GLASS) {
            return@addListener
        }

        val block = getLambdaBlock(event.block)

        val symbol = createLambdaSymbol(block, event.blockPosition, instance)

        val linked = block.withTag(Tag.UUID("link"), symbol)

        instance.setBlock(below, linked)

    }

    eventHandler.addListener(PlayerBlockBreakEvent::class.java) { event ->
        event.isCancelled = true
        val instance = event.instance
        instance.getEntityByUuid(event.block.getTag(Tag.UUID("link")))?.remove()

        instance.setBlock(event.blockPosition, Block.BLACK_STAINED_GLASS)
    }
}

fun createLambdaSymbol(block: Block, pos: BlockVec, instance: Instance): UUID {
    val display = Entity(EntityType.TEXT_DISPLAY)
    val meta = display.entityMeta as TextDisplayMeta
    meta.text = getLambdaSymbol(block)
    meta.scale = Vec(7.0,7.0,7.0)
    meta.isShadow = true
    meta.backgroundColor = 0x00000000
    display.setInstance(instance, pos.add(0.0,0.5,0.0))
    return display.uuid
}

fun getLambdaBlock(block: Block): Block {
    return when (block) {
        Block.STICKY_PISTON -> Block.STICKY_PISTON.withProperty("facing", "east")
        Block.PISTON -> Block.PISTON.withProperty("facing", "west")
        else -> block
    }
}

fun getLambdaSymbol(block: Block): Component {
    return Component.text(when (block.namespace().toString()) {
        "minecraft:diamond_block" -> "!"
        "minecraft:sticky_piston" -> "("
        "minecraft:piston" -> ")"
        "minecraft:glowstone" -> "λ"
        "minecraft:stone" -> "."
        "minecraft:red_wool" -> "a"
        "minecraft:green_wool" -> "b"
        "minecraft:blue_wool" -> "c"
        else -> ""
    })
}