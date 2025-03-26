package minestom

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.instance.InstanceContainer

fun onJoin(eventHandler: GlobalEventHandler, instanceContainer: InstanceContainer) {
    eventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        val player = event.player
        event.spawningInstance = instanceContainer
        player.respawnPoint = Pos(0.0, 41.0, 0.0)
        player.gameMode = GameMode.CREATIVE
    }

    eventHandler.addListener(PlayerSkinInitEvent::class.java) { event ->
        event.skin = PlayerSkin.fromUsername(event.player.username)
    }
}