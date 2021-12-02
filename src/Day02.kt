private const val SLIDING_WINDOW_SIZE = 3

fun main() {
    fun part1(input: List<String>): Int {
        val commandsGroups = input
            .map {
                val pair = it.split(' ')
                pair[0] to pair[1].toInt()
            }
            .groupBy { it.first }

        val forward = commandsGroups["forward"]?.sumOf { it.second } ?: 0
        val down = commandsGroups["down"]?.sumOf { it.second } ?: 0
        val up = commandsGroups["up"]?.sumOf { it.second } ?: 0
        return forward * (down - up)
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var aim = 0
        var horizontal = 0

        input.forEach {
            val pair = it.split(' ')
            val command = pair[0]
            val value = pair[1].toInt()
            when (command) {
                "down" -> aim += value
                "up" -> aim -= value
                "forward" -> {
                    depth += aim * value
                    horizontal += value
                }
            }
        }

        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
