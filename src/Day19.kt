import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {
    operator fun Triple<Int, Int, Int>.minus(other: Triple<Int, Int, Int>) = Triple(
        first - other.first,
        second - other.second,
        third - other.third
    )

    operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) = Triple(
        first + other.first,
        second + other.second,
        third + other.third
    )

    fun allCombinations(points: List<Triple<Int, Int, Int>>): List<List<Triple<Int, Int, Int>>> {
        return listOf(
            // +X
            points.toList(),
            points.map { it.copy(second = -it.third, third = it.second) },
            points.map { it.copy(second = -it.second, third = -it.third) },
            points.map { it.copy(second = it.third, third = -it.second) },
            // -X
            points.map { it.copy(first = -it.first, second = -it.second) },
            points.map { it.copy(first = -it.first, second = it.third, third = it.second) },
            points.map { it.copy(first = -it.first, second = it.second, third = -it.third) },
            points.map { it.copy(first = -it.first, second = -it.third, third = -it.second) },
            // +Y
            points.map { it.copy(first = it.second, second = it.third, third = it.first) },
            points.map { it.copy(first = it.second, second = -it.first, third = it.third) },
            points.map { it.copy(first = it.second, second = -it.third, third = -it.first) },
            points.map { it.copy(first = it.second, second = it.first, third = -it.third) },
            // -Y
            points.map { it.copy(first = -it.second, second = -it.third, third = it.first) },
            points.map { it.copy(first = -it.second, second = it.first, third = it.third) },
            points.map { it.copy(first = -it.second, second = it.third, third = -it.first) },
            points.map { it.copy(first = -it.second, second = -it.first, third = -it.third) },
            // +Z
            points.map { it.copy(first = it.third, second = it.first, third = it.second) },
            points.map { it.copy(first = it.third, second = -it.second, third = it.first) },
            points.map { it.copy(first = it.third, second = -it.first, third = -it.second) },
            points.map { it.copy(first = it.third, second = it.second, third = -it.first) },
            // -Z
            points.map { it.copy(first = -it.third, second = -it.first, third = it.second) },
            points.map { it.copy(first = -it.third, second = it.second, third = it.first) },
            points.map { it.copy(first = -it.third, second = it.first, third = -it.second) },
            points.map { it.copy(first = -it.third, second = -it.second, third = -it.first) },
        )
    }

    fun part1(input: List<String>): Int {
        val scanners = mutableListOf<MutableList<Triple<Int, Int, Int>>>()
        input.filter { it.isNotEmpty() }
            .forEach {
                if (it.startsWith("---")) {
                    scanners.add(mutableListOf())
                } else {
                    val arr = it.split(',').map { s -> s.toInt() }
                    scanners.last().add(Triple(arr[0], arr[1], arr[2]))
                }
            }

        val notMerged = scanners.subList(1, scanners.size).toMutableList()
        val merged = scanners[0].toMutableList()
        while (notMerged.isNotEmpty()) {
            val notMergedInAllRotations = notMerged.map { allCombinations(it) }
            val found = notMergedInAllRotations.firstOrNull { allCombinations ->
                allCombinations.any { beacons ->
                    beacons.any { beacon ->
                        merged.any { anchor ->
                            val centerizedBeacons = beacons.map { it - beacon }
                            val centerizedMerged = merged.map { it - anchor }
                            val overlapCount = centerizedBeacons.count { it in centerizedMerged }
                            if (overlapCount >= 12) {
                                val newPoints = centerizedBeacons - centerizedMerged
                                merged += newPoints.map { newPoint -> newPoint + anchor }
                            }
                            overlapCount >= 12
                        }
                    }
                }
            }
            found?.let {
                val idx = notMergedInAllRotations.indexOf(found)
                notMerged.removeAt(idx)
            }
        }

        return merged.count()
    }

    fun part2(input: List<String>): Int {
        val scanners = mutableListOf<MutableList<Triple<Int, Int, Int>>>()
        input.filter { it.isNotEmpty() }
            .forEach {
                if (it.startsWith("---")) {
                    scanners.add(mutableListOf())
                } else {
                    val arr = it.split(',').map { s -> s.toInt() }
                    scanners.last().add(Triple(arr[0], arr[1], arr[2]))
                }
            }

        val notMerged = scanners.subList(1, scanners.size).toMutableList()
        val merged = scanners[0].toMutableList()
        val scannersCenters = mutableListOf(Triple(0, 0, 0))
        while (notMerged.isNotEmpty()) {
            val notMergedInAllRotations = notMerged.map { allCombinations(it) }
            val found = notMergedInAllRotations.firstOrNull { allCombinations ->
                allCombinations.any { beacons ->
                    beacons.any { beacon ->
                        merged.any { anchor ->
                            val possibleScanner = anchor - beacon
                            val centerizedBeacons = beacons.map { it - beacon }
                            val centerizedMerged = merged.map { it - anchor }
                            val overlapCount = centerizedBeacons.count { it in centerizedMerged }
                            if (overlapCount >= 12) {
                                val newPoints = centerizedBeacons - centerizedMerged
                                merged += newPoints.map { newPoint -> newPoint + anchor }
                                scannersCenters += possibleScanner
                            }
                            overlapCount >= 12
                        }
                    }
                }
            }
            found?.let {
                val idx = notMergedInAllRotations.indexOf(found)
                notMerged.removeAt(idx)
            }
        }

        var max = 0
        scannersCenters.forEach { c1 ->
            scannersCenters.forEach { c2 ->
                val dd = c1 - c2
                max = max(max, dd.first.absoluteValue + dd.second.absoluteValue + dd.third.absoluteValue)
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
