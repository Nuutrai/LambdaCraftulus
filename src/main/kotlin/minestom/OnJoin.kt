package minestom

import me.chriss99.minestom.BlockSymbol
import me.chriss99.minestom.createBasePlate
import me.chriss99.minestom.createCursor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.item.ItemStack

fun onJoin(eventHandler: GlobalEventHandler, instanceContainer: InstanceContainer) {

    eventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        event.spawningInstance = instanceContainer
        event.player.respawnPoint = Pos(0.0, 41.0, 0.0)
    }

    eventHandler.addListener(PlayerLoadedEvent::class.java) { event ->
        val player = event.player

        player.gameMode = GameMode.ADVENTURE
        BlockSymbol.entries.forEach { blockSymbol ->
            val material = blockSymbol.block.registry().material() ?: return@forEach
            val item = ItemStack.of(material)
            player.inventory.addItemStack(item)
        }
        player.isFlying = true
        createBasePlate(player, instanceContainer)
        createCursor(player, instanceContainer)
    }

    eventHandler.addListener(PlayerSkinInitEvent::class.java) { event ->
        event.skin = PlayerSkin.fromUsername(event.player.username)
    }

}
