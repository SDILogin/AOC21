fun main() {
    fun part1(input: List<String>): Int {
        val arr = Array(101) { Array(101) { BooleanArray(101) { false } } }
        input.forEach { inst ->
            val zRangeStrArr = inst.substringAfter(",z=").split("..")
            val yRangeStrArr = inst.substringBefore(",z=").substringAfter(",y=").split("..")
            val xRangeStrArr = inst.substringBefore(",y=").substringAfter("x=").split("..")
            val state = inst.substringBefore("x=").trim()

            val zMin = maxOf(zRangeStrArr[0].toInt() + 50, 0)
            val zMax = minOf(zRangeStrArr[1].toInt() + 50, 100)

            val yMin = maxOf(yRangeStrArr[0].toInt() + 50, 0)
            val yMax = minOf(yRangeStrArr[1].toInt() + 50, 100)

            val xMin = maxOf(xRangeStrArr[0].toInt() + 50, 0)
            val xMax = minOf(xRangeStrArr[1].toInt() + 50, 100)

            for (z in zMin..zMax) for (y in yMin..yMax) for (x in xMin..xMax) {
                arr[x][y][z] = state == "on"
            }
        }
        return arr.sumOf { arr2d -> arr2d.sumOf { boolVec -> boolVec.count { it } } }
    }

    fun part2(input: List<String>): Long {
        fun merge(boxes: List<Box>, newBox: Box): List<Box> {
            val exclude = boxes
                .filter { it.isIntersect(newBox) }
                .map { it.intersectBox(newBox).copy(on = !it.on) }

            return if (newBox.on) boxes + newBox + exclude else boxes + exclude
        }

        var boxes = listOf<Box>()
        input.forEach { inst ->
            val zRangeStrArr = inst
                .substringAfter(",z=")
                .split("..")
                .map { it.toInt() }
            val yRangeStrArr = inst
                .substringBefore(",z=")
                .substringAfter(",y=")
                .split("..")
                .map { it.toInt() }
            val xRangeStrArr = inst
                .substringBefore(",y=")
                .substringAfter("x=")
                .split("..")
                .map { it.toInt() }
            val state = inst.substringBefore("x=").trim()
            val newBox = Box(
                on = state == "on",
                minPoint = Point(
                    x = xRangeStrArr[0],
                    y = yRangeStrArr[0],
                    z = zRangeStrArr[0]
                ),
                maxPoint = Point(
                    x = xRangeStrArr[1],
                    y = yRangeStrArr[1],
                    z = zRangeStrArr[1]
                )
            )
            boxes = merge(boxes, newBox)
        }
        return boxes.fold(0L) { acc, box -> if (box.on) acc + box.volume() else acc - box.volume() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part2(testInput) == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

data class Point(
    val x: Int,
    val y: Int,
    val z: Int,
)

data class Box(
    val on: Boolean,
    val minPoint: Point,
    val maxPoint: Point,
) {
    fun volume(): Long {
        // +1. More than 3 hours of debug. I have never felt myself so stupid *facepalm*
        val xLen = maxPoint.x - minPoint.x + 1L
        val yLen = maxPoint.y - minPoint.y + 1L
        val zLen = maxPoint.z - minPoint.z + 1L
        return xLen * yLen * zLen
    }

    fun isIntersect(box: Box): Boolean {
        if (box.minPoint.x > maxPoint.x) return false
        if (box.maxPoint.x < minPoint.x) return false

        if (box.minPoint.y > maxPoint.y) return false
        if (box.maxPoint.y < minPoint.y) return false

        if (box.minPoint.z > maxPoint.z) return false
        if (box.maxPoint.z < minPoint.z) return false

        return true
    }

    fun intersectBox(box: Box): Box {
        val xArr = listOf(minPoint.x, maxPoint.x, box.minPoint.x, box.maxPoint.x).sorted()
        val yArr = listOf(minPoint.y, maxPoint.y, box.minPoint.y, box.maxPoint.y).sorted()
        val zArr = listOf(minPoint.z, maxPoint.z, box.minPoint.z, box.maxPoint.z).sorted()
        return Box(
            on = box.on,
            minPoint = Point(
                x = xArr[1],
                y = yArr[1],
                z = zArr[1],
            ),
            maxPoint = Point(
                x = xArr[2],
                y = yArr[2],
                z = zArr[2],
            )
        )
    }
}
