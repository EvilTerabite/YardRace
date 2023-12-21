package cc.yardpodcast.yardrace.listeners

import cc.yardpodcast.yardrace.util.M
import cc.yardpodcast.yardrace.util.Zone
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class PlayerListener : Listener {



    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player


        if (event.hasItem() && event.item == createLineStick() && player.hasPermission("yardrace.admin")) {
            if (event.action.name.contains("LEFT")) {
                selectFirstLocation(player, event.clickedBlock?.location)
            }

            if (event.action.name.contains("RIGHT")) {
                selectSecondLocation(player, event.clickedBlock?.location)
            }

            event.isCancelled = true
        }
    }

    private fun selectFirstLocation(player: Player, location: Location?) {
        selectedLocations[player] = Pair(location!!, null)
        M.sendMessage(player, "First location selected!")
    }

    private fun selectSecondLocation(player: Player, location: Location?) {
        if (selectedLocations[player] == null) {
            M.sendMessage(player, "You must select the first location first!")
            return
        }
        selectedLocations[player] = Pair(selectedLocations[player]!!.first, location)
        M.sendMessage(player, "Second location selected!")
    }
companion object {

    private val selectedLocations = mutableMapOf<Player, Pair<Location, Location?>>()

    fun getZone(player: Player): Zone {
        return Zone(selectedLocations[player]!!.first, selectedLocations[player]!!.second!!)
    }

    fun createLineStick(): ItemStack {
        val item = ItemStack(Material.STICK)
        val meta = item.itemMeta
        meta.displayName(Component.text("Line Stick"))
        item.itemMeta = meta
        return item
    }
}
}