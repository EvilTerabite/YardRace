package cc.yardpodcast.yardrace.util


import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block


class Zone(val world: World, x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {
    val minX: Int
    val maxX: Int
    val minY: Int
    val maxY: Int
    val minZ: Int
    val maxZ: Int
    var loc1: Location
    var loc2: Location

    constructor(loc1: Location, loc2: Location) : this(
        loc1.world,
        loc1.blockX,
        loc1.blockY,
        loc1.blockZ,
        loc2.blockX,
        loc2.blockY,
        loc2.blockZ
    )

    init {
        minX = Math.min(x1, x2)
        minY = Math.min(y1, y2)
        minZ = Math.min(z1, z2)
        maxX = Math.max(x1, x2)
        maxY = Math.max(y1, y2)
        maxZ = Math.max(z1, z2)
        loc1 = Location(world, x1.toDouble(), y1.toDouble(), z1.toDouble())
        loc2 = Location(world, x2.toDouble(), y2.toDouble(), z2.toDouble())
    }

    operator fun contains(Zone: Zone): Boolean {
        return Zone.world == world && Zone.minX >= minX && Zone.maxX <= maxX && Zone.minY >= minY && Zone.maxY <= maxY && Zone.minZ >= minZ && Zone.maxZ <= maxZ
    }

    operator fun contains(location: Location): Boolean {
        return contains(location.blockX, location.blockY, location.blockZ)
    }

    fun contains(x: Int, y: Int, z: Int): Boolean {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ
    }

    fun overlaps(Zone: Zone): Boolean {
        return Zone.world == world &&
                !(Zone.minX > maxX || Zone.minY > maxY || Zone.minZ > maxZ || minZ > Zone.maxX || minY > Zone.maxY || minZ > Zone.maxZ)
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj !is Zone) {
            return false
        }
        val other = obj
        return world == other.world && minX == other.minX && minY == other.minY && minZ == other.minZ && maxX == other.maxX && maxY == other.maxY && maxZ == other.maxZ
    }

    override fun toString(): String {
        return world.name +
                "," + minX +
                "," + minY +
                "," + minZ +
                "," + maxX +
                "," + maxY +
                "," + maxZ
    }

    fun select(): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val b = Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
                    blocks.add(b)
                }
            }
        }
        return blocks
    }

    companion object {
        fun select(loc1: Location, loc2: Location, w: World?): List<Block> {
            val blocks: MutableList<Block> = ArrayList()
            val x1 = loc1.blockX
            val y1 = loc1.blockY
            val z1 = loc1.blockZ
            val x2 = loc2.blockX
            val y2 = loc2.blockY
            val z2 = loc2.blockZ
            val xMin: Int
            val yMin: Int
            val zMin: Int
            val xMax: Int
            val yMax: Int
            val zMax: Int
            var x: Int
            var y: Int
            var z: Int
            if (x1 > x2) {
                xMin = x2
                xMax = x1
            } else {
                xMin = x1
                xMax = x2
            }
            if (y1 > y2) {
                yMin = y2
                yMax = y1
            } else {
                yMin = y1
                yMax = y2
            }
            if (z1 > z2) {
                zMin = z2
                zMax = z1
            } else {
                zMin = z1
                zMax = z2
            }
            x = xMin
            while (x <= xMax) {
                y = yMin
                while (y <= yMax) {
                    z = zMin
                    while (z <= zMax) {
                        val b = Location(w, x.toDouble(), y.toDouble(), z.toDouble()).block
                        blocks.add(b)
                        z++
                    }
                    y++
                }
                x++
            }
            return blocks
        }
    }
}