package com.thedeafcreeper.fireconversion

import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.max
import kotlin.math.min

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

        if (!conversions.containsKey(itemStack.type)) return

        val conversion = conversions[itemStack.type]!!
        val fireType = entity.world.getType(entity.location)
        entity.isInvulnerable = true

        val toSpawn: MutableMap<Material, Int> = EnumMap(org.bukkit.Material::class.java)

        when (fireType) {
            Material.FIRE -> {
                if (conversion.regularResults.isEmpty()) return

                for (i in 0 until itemStack.amount) {
                    val chosenResult = conversion.regularResults.random()
                    toSpawn[chosenResult.material] = toSpawn.getOrDefault(chosenResult.material, 0) + chosenResult.amount
                }

                entity.remove()
            }
            Material.SOUL_FIRE -> {
                if (conversion.soulResults.isEmpty()) return

                for (i in 0 until itemStack.amount) {
                    val chosenResult = conversion.soulResults.random()
                    toSpawn[chosenResult.material] = toSpawn.getOrDefault(chosenResult.material, 0) + chosenResult.amount
                }
            }
            else -> {
                event.isCancelled = true
                return
            }
        }

        for ((mat, amount) in toSpawn) {
            var totalSpawned = 0

            while (totalSpawned < amount) {
                val spawnNow = min(64, amount - totalSpawned)
                val newItem = entity.world.dropItem(entity.location, ItemStack(mat, spawnNow))
                newItem.isInvulnerable = true
                newItem.velocity = entity.velocity
                newItem.thrower = entity.thrower
                newItem.pickupDelay = entity.pickupDelay

                totalSpawned += spawnNow
            }
        }
        entity.remove()
    }

    class Conversion(regularFire: MutableList<String>, soulFire: MutableList<String>, private val invalidMaterials: List<Material>) {
        val regularResults: MutableList<Result> = getResults(regularFire)
        val soulResults: MutableList<Result> = getResults(soulFire)

        private fun getResults(strings: MutableList<String>): MutableList<Result> {
            val results: MutableList<Result> = ArrayList()

            for (string in strings) {
                val split = string.split("-")
                val material = if (split.isNotEmpty()) Material.getMaterial(split[0]) ?: continue else continue

                if (invalidMaterials.contains(material)) continue

                val amount = if (split.size > 1) max(split[1].toIntOrNull() ?: 1, 1) else 1
                val weight = if (split.size > 2) max(split[2].toIntOrNull() ?: 1, 1) else 1

                val result = Result(material, amount)

                for (i in 0 until weight) {
                    results.add(result)
                }
            }

            return results
        }
    }

    data class Result(val material: Material, val amount: Int)
}