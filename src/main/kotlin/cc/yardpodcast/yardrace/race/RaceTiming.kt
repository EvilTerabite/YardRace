package cc.yardpodcast.yardrace.race

import cc.yardpodcast.yardrace.util.M
import org.bukkit.entity.Player
import java.util.UUID

class RaceTiming {
    companion object {
        private val timedPlayers = mutableMapOf<UUID, Long>()
        fun startLapTimer(player: Player) {
            if(isTimedLap(player)) return
            timedPlayers[player.uniqueId] = System.currentTimeMillis()
            M.sendMessage(player, "Timer started!")
        }

        fun endLapTimer(player: Player): Long? {
            val startTime = timedPlayers[player.uniqueId] ?: return null
            val endTime = System.currentTimeMillis()
            M.sendMessage(player, "Your time was ${formatTime(endTime - startTime)}")

            timedPlayers.remove(player.uniqueId)
            return endTime - startTime
        }

        fun resetLapTimer(player: Player) {
            timedPlayers.remove(player.uniqueId)
        }

        fun isTimedLap(player: Player): Boolean {
            return timedPlayers.containsKey(player.uniqueId)
        }

        fun getLapTimer(player: Player): Long? {
            return timedPlayers[player.uniqueId]
        }

        fun formatTime(time: Long): String {
            //format time in ss:ms
            val seconds = time / 1000
            val milliseconds = time % 1000
            return "${seconds}.${milliseconds}"
        }
    }
}