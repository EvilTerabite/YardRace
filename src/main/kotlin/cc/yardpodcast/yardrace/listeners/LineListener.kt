package cc.yardpodcast.yardrace.listeners

import cc.yardpodcast.yardrace.Main
import cc.yardpodcast.yardrace.race.RaceTiming
import cc.yardpodcast.yardrace.util.Configuration
import cc.yardpodcast.yardrace.util.M
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.UUID

class LineListener : Listener {
    private val cooldownPlayers = mutableListOf<UUID>()
    private val checkpointPlayers = mutableMapOf<UUID, Int>()
    private val playerLapMap = mutableMapOf<UUID, Int>()
    private val finishers = mutableListOf<UUID>()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val location = player.location

        //Start Zone Listener
        if(Main.startRaceZone?.contains(location) == true) {
            if(!player.isInsideVehicle) {
                player.world.spawnEntity(location, EntityType.BOAT).addPassenger(player)
                playerBoatMap[player.uniqueId] = player.vehicle?.uniqueId ?: return
            }
        }

        if(RaceTiming.isTimedLap(player) && !player.isInsideVehicle) {
            RaceTiming.resetLapTimer(player)
            teleportPlayerToLobby(player)
            M.sendMessage(player, "You left the boat!")
        }
        if(!event.player.isInsideVehicle) return

        //Start Line Listener
        if(Main.startLine?.contains(location) == true && !isCooldown(player)) {
            if(RaceTiming.isTimedLap(player)) {
                RaceTiming.resetLapTimer(player)
            }
            RaceTiming.startLapTimer(player)
            addCooldown(player)
            checkpointPlayers[player.uniqueId] = 0
        }

        //Finish Line Listener
        if(Main.finishLine?.contains(location) == true) {
            if (RaceTiming.isTimedLap(player) && checkpointPlayers[player.uniqueId] == 1) {
                val time = RaceTiming.endLapTimer(player)
                if (time != null) {
                    val pb = Configuration.getPersonalBest(player)
                    if (pb != null) {
                        val delta = RaceTiming.formatTime(time - pb)
                        M.sendMessage(
                            player, if (delta.startsWith("-")) {
                                delta.replace("-", "")
                                "<green> -$delta</green>"
                            } else {
                                "<red> +$delta</red>!"
                            }
                        )
                    }
                    if (isPersonalBest(player, time)) {
                        M.sendMessage(player, "You set a new personal best!")
                    }
                    if(Main.timeTrial) {
                        teleportPlayerToLobby(player)

                        checkpointPlayers.remove(player.uniqueId)
                    } else {
                        completeLap(player)
                    }
                }
            }
        }

        //Checkpoint Line Listener
        if(Main.checkpointLine?.contains(location) == true) {
            if (RaceTiming.isTimedLap(player) && !checkpointPlayers.contains(player.uniqueId)) {
                M.sendMessage(player, "Checkpoint reached!")
                checkpointPlayers[player.uniqueId] = checkpointPlayers[player.uniqueId]!! + 1
            }
        }
    }

    private fun addCooldown(player: Player) {
        cooldownPlayers.add(player.uniqueId)
        Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
            cooldownPlayers.remove(player.uniqueId)
        }, 20)
    }

    private fun isCooldown(player: Player): Boolean {
        return cooldownPlayers.contains(player.uniqueId)
    }

    private fun isPersonalBest(player: Player, time: Long): Boolean {
        return if (Configuration.getPersonalBest(player) != null) {
            val personalBest = Configuration.getPersonalBest(player)!!
            if (time < personalBest) {
                Configuration.logPersonalBest(player, time)
                true
            } else {
                false
            }
        } else {
            Configuration.logPersonalBest(player, time)
            true
        }
    }

    private fun completeLap(player: Player) {
        if(playerLapMap[player.uniqueId] == Main.lapCount) {
            completeRace(player)
        }
        if(!playerLapMap.containsKey(player.uniqueId)) {
            playerLapMap[player.uniqueId] = 1
        } else {
            playerLapMap[player.uniqueId] = playerLapMap[player.uniqueId]!! + 1
        }

        M.sendMessage(player, "Lap ${playerLapMap[player.uniqueId]}/${Main.lapCount} completed!")
    }

    private fun completeRace(player: Player) {
        finishers.add(player.uniqueId)
        M.sendMessage(player, "You finished in place ${finishers.indexOf(player.uniqueId) + 1}!")
        if(finishers.size == playersInRace.size) {
            M.sendMessage(player, "Everyone has finished!")
            finishers.clear()
            playersInRace.forEach { p ->
                teleportPlayerToLobby(Bukkit.getPlayer(p)!!)
            }

            checkpointPlayers.clear()
        }
    }

    companion object {
        val playersInRace = mutableListOf<UUID>()
        val playerBoatMap = mutableMapOf<UUID, UUID>()

        fun teleportPlayerToLobby(player: Player) {
            if(Main.lobbyLocation != null) {
                player.teleport(Main.lobbyLocation!!)
                player.world.getEntity(playerBoatMap[player.uniqueId]!!)?.remove()
                playerBoatMap.remove(player.uniqueId)
                if(player.isInsideVehicle) {
                    player.vehicle?.remove()
                }
                return
            }
        }
    }
}