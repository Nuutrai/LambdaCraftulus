package minestom

import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.BlockVec
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.Weather
import net.minestom.server.instance.block.Block
import net.minestom.server.registry.DynamicRegistry
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biome.Biome
import net.minestom.server.world.biome.BiomeEffects

val BASE_BLOCK: Block = Block.GRAY_CONCRETE

fun initInstance(): InstanceContainer {
     val dimension = DimensionType.builder()
         .height(400)
         .minY(-64)
         .logicalHeight(400)
         .ambientLight(15f)
         .build()
    val dimKey = MinecraftServer.getDimensionTypeRegistry().register("void", dimension)
    val instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(dimKey)
    instanceContainer.timeRate = 0
    instanceContainer.time = 6000
    instanceContainer.weather = Weather.RAIN
    instanceContainer.setGenerator { unit ->
        unit.modifier().fillBiome(DynamicRegistry.Key.of("void"))
        if (unit.absoluteStart().z() != 0.0) return@setGenerator
        unit.modifier().fill(
            BlockVec(0, 0, 13).add(unit.absoluteStart()),
            BlockVec(16, 328, 14).add(unit.absoluteStart()),
            BASE_BLOCK
        )
    }
    instanceContainer.loadChunk(0, 0)
    return instanceContainer
}

fun createVoidBiome() {
    val biome = Biome.builder().effects(
        BiomeEffects.builder()
            .skyColor(NamedTextColor.BLACK)
            .fogColor(NamedTextColor.BLACK)
            .waterColor(NamedTextColor.BLACK)
            .waterFogColor(NamedTextColor.BLACK)
            .build()
        )
        .hasPrecipitation(false)
        .build()
    val biomeRegistry = MinecraftServer.getBiomeRegistry()
    biomeRegistry.register("void", biome)
}
