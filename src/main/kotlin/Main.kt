package me.chriss99

import me.chriss99.minestom.LambdaBlockManager
import me.chriss99.minestom.LambdaEventHandler
import minestom.createVoidBiome
import minestom.initInstance
import minestom.onJoin
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth

fun main() {
    val minecraftServer = MinecraftServer.init()
    val eventHandler = MinecraftServer.getGlobalEventHandler()
    createVoidBiome()
    val instanceContainer = initInstance()
    onJoin(eventHandler, instanceContainer)
    LambdaEventHandler(eventHandler)
        .registerEvents()
//    MojangAuth.init()
    minecraftServer.start("0.0.0.0", 25565)
}