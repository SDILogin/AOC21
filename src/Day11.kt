import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    fun part1(input: List<String>, steps: Int): Int {
        fun flash(mtx: Array<IntArray>, row: Int, col: Int) {
            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.addLast(Pair(row, col))
            while (queue.isNotEmpty()) {
                val (y, x) = queue.removeLast()
                mtx[y][x] = Integer.MIN_VALUE
                for (i in y - 1..y + 1) for (j in x - 1..x + 1) {
                    if (i == y && j == x) continue
                    ++mtx[i][j]
                    if (mtx[i][j] == 10) queue.addLast(Pair(i, j))
                }
            }
        }

        fun incAll(mtx: Array<IntArray>) {
            for (i in 1 until mtx.size - 1) for (j in 1 until mtx[i].size - 1) {
                ++mtx[i][j]
            }
        }

        val n = 10 + 2
        val m = 10 + 2
        val mtx = Array(n) { IntArray(m) { Integer.MIN_VALUE } }
        input.forEachIndexed { i, s ->
            s.toCharArray().map { it.digitToInt() }.forEachIndexed { j, d -> mtx[i + 1][j + 1] = d }
        }

        var result = 0
        repeat(steps) {
            incAll(mtx)
            for (i in 1 until n - 1) for (j in 1 until m - 1) {
                if (mtx[i][j] > 9) flash(mtx, i, j)
            }

            for (i in 1 until n - 1) for (j in 1 until m - 1) {
                if (mtx[i][j] < 0) {
                    mtx[i][j] = 0
                    ++result
                }
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        fun flash(mtx: Array<IntArray>, row: Int, col: Int) {
            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.addLast(Pair(row, col))
            while (queue.isNotEmpty()) {
                val (y, x) = queue.removeLast()
                mtx[y][x] = Integer.MIN_VALUE
                for (i in y - 1..y + 1) for (j in x - 1..x + 1) {
                    if (i == y && j == x) continue
                    ++mtx[i][j]
                    if (mtx[i][j] == 10) queue.addLast(Pair(i, j))
                }
            }
        }

        fun incAll(mtx: Array<IntArray>) {
            for (i in 1 until mtx.size - 1) for (j in 1 until mtx[i].size - 1) {
                ++mtx[i][j]
            }
        }

        val n = 10 + 2
        val m = 10 + 2
        val mtx = Array(n) { IntArray(m) { Integer.MIN_VALUE } }
        input.forEachIndexed { i, s ->
            s.toCharArray().map { it.digitToInt() }.forEachIndexed { j, d -> mtx[i + 1][j + 1] = d }
        }

        var result = 0
        while (true) {
            ++result
            incAll(mtx)
            for (i in 1 until n - 1) for (j in 1 until m - 1) {
                if (mtx[i][j] > 9) flash(mtx, i, j)
            }

            var stop = true
            for (i in 1 until n - 1) for (j in 1 until m - 1) {
                if (mtx[i][j] < 0) {
                    mtx[i][j] = 0
                } else {
                    stop = false
                }
            }

            if (stop) return result
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input, 100))
    println(part2(input))
}
