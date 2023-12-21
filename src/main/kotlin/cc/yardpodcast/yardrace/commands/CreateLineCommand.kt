package cc.yardpodcast.yardrace.commands

import cc.yardpodcast.yardrace.Main
import cc.yardpodcast.yardrace.listeners.PlayerListener
import cc.yardpodcast.yardrace.util.Configuration
import cc.yardpodcast.yardrace.util.M
import cc.yardpodcast.yardrace.util.Zone
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateLineCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            M.sendMessage(sender, "You must be a player to use this command!")
            return true
        }

        if(args.isEmpty()) {
            sender.inventory.addItem(PlayerListener.createLineStick())
            return true
        }

        val lineType = args[0]
        if(lineType == "start") {
            M.sendMessage(sender, "Start line created!")
            Main.startLine = PlayerListener.getZone(sender)
        } else if(lineType == "finish") {
            M.sendMessage(sender, "Finish line created!")
            Main.finishLine = PlayerListener.getZone(sender)
        } else if(lineType == "checkpoint") {
            M.sendMessage(sender, "Checkpoint line created!")
            Main.checkpointLine = PlayerListener.getZone(sender)
        } else if(lineType == "startZone") {
            M.sendMessage(sender, "Start zone created!")
            Main.startRaceZone = PlayerListener.getZone(sender)
        } else if(lineType == "lobby") {
            M.sendMessage(sender, "Lobby location set!")
            Main.lobbyLocation = sender.location
        } else {
            M.sendMessage(sender, "Invalid line type!")
        }

        Configuration.saveLines()
        return true
    }
}