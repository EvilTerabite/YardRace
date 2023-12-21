package cc.yardpodcast.yardrace.commands

import cc.yardpodcast.yardrace.util.Configuration
import cc.yardpodcast.yardrace.util.M
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PersonalBestCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            M.sendMessage(sender, "You must be a player to use this command!")
            return true
        }

        if(args.isEmpty()) {
            M.sendMessage(sender, "Your personal best is ${Configuration.getPersonalBest(sender)}")
            return true
        }

        if(args[0] == "wr") {
            M.sendMessage(sender, "The world record is ${Configuration.getWR()} placed by ${Configuration.getWRPlayer()})")
            return true
        }

        M.sendMessage(sender, "That player has a personal best of ${Configuration.getPersonalBest(Bukkit.getOfflinePlayer(args[0]))}")
        return true
    }
}