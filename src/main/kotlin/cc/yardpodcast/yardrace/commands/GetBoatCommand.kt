package cc.yardpodcast.yardrace.commands

import cc.yardpodcast.yardrace.listeners.LineListener
import cc.yardpodcast.yardrace.util.M
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class GetBoatCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            M.sendMessage(sender, "You must be a player to use this command!")
            return true
        }
        if(!sender.hasPermission("yardrace.getboat")) {
            M.sendMessage(sender, "You do not have permission to use this command!")
            return true
        }

        if(sender.isInsideVehicle) {
            M.sendMessage(sender, "You are already in a boat!")
            return true
        }

        val entity = sender.world.spawnEntity(sender.location, EntityType.BOAT)
        entity.addPassenger(sender)
        LineListener.playerBoatMap[sender.uniqueId] = entity.uniqueId
        return true
    }
}