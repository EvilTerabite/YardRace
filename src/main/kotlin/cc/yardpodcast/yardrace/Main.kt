package cc.yardpodcast.yardrace

import cc.yardpodcast.yardrace.commands.CreateLineCommand
import cc.yardpodcast.yardrace.commands.GetBoatCommand
import cc.yardpodcast.yardrace.listeners.LineListener
import cc.yardpodcast.yardrace.listeners.PlayerListener
import cc.yardpodcast.yardrace.race.RaceTiming
import cc.yardpodcast.yardrace.util.Configuration
import cc.yardpodcast.yardrace.util.M
import cc.yardpodcast.yardrace.util.Zone
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {


    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        M.sendConsoleMessage("Loaded Successfully!")

        //Schedulers
        startHotbarTimerTask()
        startZoneParticlesTask()

        //Listeners
        Bukkit.getPluginManager().registerEvents(LineListener(), this)
        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)

        //Commands
        getCommand("createline")?.setExecutor(CreateLineCommand())
        getCommand("getboat")?.setExecutor(GetBoatCommand())

        //Load Lines
        try {
            Configuration.getLines()
            M.sendConsoleMessage("Loaded lines successfully!")
        } catch (e: Exception) {
            M.sendConsoleMessage("No lines found!")
        }
    }

    override fun onDisable() {
        // disable logic
    }

    private fun startHotbarTimerTask(): Int {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            Bukkit.getOnlinePlayers().forEach { player ->
                if(startLine != null || finishLine != null || checkpointLine != null) {
                    if (RaceTiming.isTimedLap(player)) {
                        M.sendActionBar(
                            player,
                            "<yellow>Timer:</yellow> <green>${
                                RaceTiming.formatTime(
                                    System.currentTimeMillis() - RaceTiming.getLapTimer(player)!!
                                )
                            }</green>"
                        )
                    }
                }
            }
        }, 0, 2).taskId
    }

    private fun startZoneParticlesTask(): Int {
        return Bukkit.getScheduler().runTaskTimer(this, Runnable {
            startLine?.select()?.forEach { block ->
                block.world.spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, block.location, 1)
            }

            finishLine?.select()?.forEach { block ->
                block.world.spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, block.location, 1)
            }

            checkpointLine?.select()?.forEach { block ->
                block.world.spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, block.location, 1)
            }

            startRaceZone?.select()?.forEach { block ->
                block.world.spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, block.location, 1)
            }
        }, 0, 20).taskId
    }

    companion object {
        var startLine: Zone? = null
        var finishLine: Zone? = null
        var checkpointLine: Zone? = null
        var startRaceZone: Zone? = null
        var lobbyLocation: Location? = null
        var lapCount: Int = 0
        var timeTrial: Boolean = true


        lateinit var instance: Main
            private set
    }


}