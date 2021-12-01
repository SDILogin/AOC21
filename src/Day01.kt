private const val SLIDING_WINDOW_SIZE = 3

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.toInt() }
            .windowed(2)
            .count { pair -> pair[0] < pair[1] }
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.toInt() }
            .windowed(SLIDING_WINDOW_SIZE)
            .map { window -> window.sum() }
            .windowed(2)
            .count { pair -> pair[0] < pair[1] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
