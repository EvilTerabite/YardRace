package cc.yardpodcast.yardrace.util

import cc.yardpodcast.yardrace.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.logging.Level

class M {
    companion object {
        private val miniMessage = MiniMessage.miniMessage()
        private val prefix = Configuration.getPluginPrefix()

        fun sendMessage(player: Player, message: String) {
            player.sendMessage(miniMessage.deserialize("$prefix $message"))
        }

        fun sendMessage(sender: CommandSender, message: String) {
            sender.sendMessage(miniMessage.deserialize("$prefix $message"))
        }

        fun sendConsoleMessage(message: String) {
            Main.instance.logger.log(Level.INFO, message)
        }

        fun sendActionBar(player: Player, message: String) {
            player.sendActionBar(miniMessage.deserialize(message))
            Main.instance.server.scheduler.runTaskLater(Main.instance, Runnable {
                player.sendActionBar(Component.empty())
            }, 20)
        }

        fun sendActionBar(player: Player, message: String, time: Long) {
            player.sendActionBar(miniMessage.deserialize(message))
            Main.instance.server.scheduler.runTaskLater(Main.instance, Runnable {
                player.sendActionBar(Component.empty())
            }, time)
        }
    }
}