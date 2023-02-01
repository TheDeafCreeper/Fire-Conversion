package com.thedeafcreeper.fireconversion

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class FireConversion : JavaPlugin() {
    private val listener = Listener()

    override fun onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(listener, this)
        loadConversion()
        logger.info("Ready!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("UnReady!")
    }

    private lateinit var conversionsFile: File
    private val conversionsConfig = YamlConfiguration()

    private fun loadConversion() {
        conversionsFile = File(dataFolder, "conversions.yml")

        if (!conversionsFile.exists()) {
            conversionsFile.parentFile.mkdirs()
            saveResource("conversions.yml", false)
        }

        try {
            conversionsConfig.load(conversionsFile)

            listener.conversions.clear()

            val keys = conversionsConfig.getKeys(false)
            for (key in keys) {
                val resultNormal = conversionsConfig.getString("$key.result.normal")
                val resultSoul = conversionsConfig.getString("$key.result.soul")
                val sources = conversionsConfig.getStringList("$key.sources")
                if (sources.isEmpty()) continue
                if (resultNormal == null && resultSoul == null) continue

                val conversion = Listener.Conversion(Material.matchMaterial(resultNormal ?: ""), Material.matchMaterial(resultSoul ?: ""))
                for (source in sources) {
                    val sourceMaterial = Material.matchMaterial(source)
                    if (sourceMaterial == null) {
                        logger.info("Invalid source material $source!")
                        continue
                    }
                    listener.conversions[sourceMaterial] = conversion
                }
            }

            logger.info("Loaded ${listener.conversions.size} conversions!")
        }
        catch (error: IOException) { logger.severe("Failed to conversions!") }
        catch (error: InvalidConfigurationException) { logger.severe("Failed to conversions!") }
    }
}