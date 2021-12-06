import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    fun part1(input: List<String>, gen: Int): Long {
        val n = 9
        val currentGen = LongArray(n)
        input[0].split(',').forEach { currentGen[it.toInt()]++ }

        repeat(gen) {
            val newborn = currentGen[0]
            for (i in 0 until n - 1) {
                currentGen[i] = currentGen[i + 1]
            }
            currentGen[n - 1] = newborn
            currentGen[n - 3] += newborn
        }

        return currentGen.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput, 256) == 26984457539)

    val input = readInput("Day06")
    println(part1(input, 80))
    println(part1(input, 256))
}
