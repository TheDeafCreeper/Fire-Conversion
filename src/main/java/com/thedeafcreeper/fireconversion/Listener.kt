package com.thedeafcreeper.fireconversion

import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent
import java.util.*

class Listener: Listener {
    var conversions: MutableMap<Material, Conversion> = EnumMap(org.bukkit.Material::class.java)

    val illegalMaterial = listOf(
        Material.AIR,
        Material.CAVE_AIR,
        Material.VOID_AIR
    )

    @EventHandler
    fun onEntityCombust(event: EntityCombustEvent) {
        if (event.entity !is Item) return

        val entity = event.entity as Item
        val itemStack = entity.itemStack

        if (conversions.containsKey(itemStack.type)) {
            val conversion = conversions[itemStack.type]!!
            val fireType = entity.world.getType(entity.location)
            entity.isInvulnerable = true

            when (fireType) {
                Material.FIRE -> {
                    if (conversion.regularFire != null && !illegalMaterial.contains(conversion.regularFire)) itemStack.type = conversion.regularFire
                }
                Material.SOUL_FIRE -> {
                    if (conversion.soulFire != null && !illegalMaterial.contains(conversion.soulFire)) itemStack.type = conversion.soulFire
                }
                else -> entity.isInvulnerable = false
            }

            entity.itemStack = itemStack
        }
    }

    data class Conversion(val regularFire: Material?, val soulFire: Material?)
}