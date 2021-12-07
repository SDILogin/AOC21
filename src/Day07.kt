import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input[0].split(",").map { it.toInt() }
        var prev = Integer.MAX_VALUE
        for (target in positions.minOf { it }..positions.maxOf { it }) {
            val current = positions.sumOf { abs(it - target) }
            if (current <= prev) prev = current else break
        }
        return prev
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(",").map { it.toInt() }
        var prev = Integer.MAX_VALUE
        for (target in positions.minOf { it }..positions.maxOf { it }) {
            val current = positions.sumOf {
                val n = abs(it - target)
                n * (n + 1) / 2
            }
            if (current <= prev) prev = current else break
        }
        return prev
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
