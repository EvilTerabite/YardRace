package cc.yardpodcast.yardrace.util

import cc.yardpodcast.yardrace.Main
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class Configuration {
    companion object {
        private val config = Main.instance.config
        fun get(value: String): Any? {
            return config.get(value)
        }

        fun getPluginPrefix(): String? {
            return config.getString("prefix")
        }

        fun logPersonalBest(player: Player, time: Long) {
            config.set("${player.uniqueId}.boatBest", time)
            Main.instance.saveConfig()
        }
        fun getWR(): Long? {
            var wr = 0L
            for(uuid in config.getKeys(false)) {
                if(wr > config.getLong("$uuid.boatBest")) {
                    wr = config.getLong("$uuid.boatBest")
                }
            }

            return wr
        }

        fun getWRPlayer(): OfflinePlayer? {
            var wr = 0L
            var wrPlayer: OfflinePlayer? = null
            for(uuid in config.getKeys(false)) {
                if(wr > config.getLong("$uuid.boatBest")) {
                    wr = config.getLong("$uuid.boatBest")
                    wrPlayer = Main.instance.server.getOfflinePlayer(uuid)
                }
            }

            return wrPlayer
        }

        fun getPersonalBest(player: OfflinePlayer): Long? {
            if(!config.contains("${player.uniqueId}.boatBest")) return null
            return config.getLong("${player.uniqueId}.boatBest")
        }

        fun saveLines() {
            //TODO: Save lines
        }

        fun getLines() {
            //TODO: Get lines
        }
    }
}