import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    fun part1(input: List<String>): Int {
        val lineParams = input.map { line ->
            val coordinates = line.split("->")
                .map { it.split(',') }
                .flatten()
                .map { it.trim().toInt() }
            Pair(coordinates[0], coordinates[1]) to Pair(coordinates[2], coordinates[3])
        }.filter { intLine ->
            val x1 = intLine.first.first
            val y1 = intLine.first.second
            val x2 = intLine.second.first
            val y2 = intLine.second.second
            x1 == x2 || y1 == y2
        }

        val n = lineParams.maxOf { max(it.first.first, it.second.first) }
        val m = lineParams.maxOf { max(it.first.second, it.second.second) }

        val mtx = Array(n + 1) { IntArray(m + 1) }
        lineParams.forEach { lineParam ->
            if (lineParam.first.first != lineParam.second.first) {
                val y = lineParam.first.second
                val xMin = min(lineParam.first.first, lineParam.second.first)
                val xMax = max(lineParam.first.first, lineParam.second.first)
                for (x in xMin..xMax) mtx[x][y]++
            } else if (lineParam.first.second != lineParam.second.second) {
                val x = lineParam.first.first
                val yMin = min(lineParam.first.second, lineParam.second.second)
                val yMax = max(lineParam.first.second, lineParam.second.second)
                for (y in yMin..yMax) mtx[x][y]++
            }
        }

        return mtx.fold(0) { acc, row -> acc + row.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val lineParams = input.map { line ->
            val coordinates = line.split("->")
                .map { it.split(',') }
                .flatten()
                .map { it.trim().toInt() }
            Pair(coordinates[0], coordinates[1]) to Pair(coordinates[2], coordinates[3])
        }

        val n = lineParams.maxOf { max(it.first.first, it.second.first) }
        val m = lineParams.maxOf { max(it.first.second, it.second.second) }

        val generateProgression: (Int, Int) -> IntProgression = { from, to ->
            if (from > to) from downTo to else from..to
        }

        val mtx = Array(n + 1) { IntArray(m + 1) }
        lineParams.forEach { lineParam ->
            if (lineParam.first.second == lineParam.second.second) {
                val y = lineParam.first.second
                val xMin = min(lineParam.first.first, lineParam.second.first)
                val xMax = max(lineParam.first.first, lineParam.second.first)
                for (x in xMin..xMax) mtx[x][y]++
            } else if (lineParam.first.first == lineParam.second.first) {
                val x = lineParam.first.first
                val yMin = min(lineParam.first.second, lineParam.second.second)
                val yMax = max(lineParam.first.second, lineParam.second.second)
                for (y in yMin..yMax) mtx[x][y]++
            } else {
                val xRange = generateProgression(lineParam.first.first, lineParam.second.first)
                val yRange = generateProgression(lineParam.first.second, lineParam.second.second)
                xRange.zip(yRange).forEach { coordinate -> mtx[coordinate.first][coordinate.second]++ }
            }
        }

        return mtx.fold(0) { acc, row -> acc + row.count { it > 1 } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
