fun main() {
    fun part1(input: List<String>): Int {
        val n = input.size + 2
        val m = input[0].length + 2
        val mtx = Array(n) {
            IntArray(m) { Integer.MAX_VALUE }
        }
        input.forEachIndexed { i, str ->
            str.toCharArray().mapIndexed { j, c ->
                mtx[i + 1][j + 1] = c.digitToInt()
            }
        }

        var totalRiskLevel = 0
        for (i in 1 until n - 1) for (j in 1 until m - 1) {
            if (mtx[i][j] < mtx[i - 1][j] && mtx[i][j] < mtx[i + 1][j] &&
                mtx[i][j] < mtx[i][j - 1] && mtx[i][j] < mtx[i][j + 1]
            ) {
                totalRiskLevel += mtx[i][j] + 1
            }
        }

        return totalRiskLevel
    }

    fun part2(input: List<String>): Int {
        val n = input.size + 2
        val m = input[0].length + 2
        val mtx = Array(n) { IntArray(m) { 9 } }
        input.forEachIndexed { i, str ->
            str.toCharArray().mapIndexed { j, c ->
                mtx[i + 1][j + 1] = c.digitToInt()
            }
        }

        val basins = mutableListOf<Int>()
        val visited = Array(n) { BooleanArray(m) { false } }
        for (i in 1 until n - 1) for (j in 1 until m - 1) {
            if (visited[i][j] || mtx[i][j] == 9) continue

            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.addLast(Pair(i, j))
            var basinSize = 0
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                ++basinSize
                visited[current.first][current.second] = true

                if (!visited[current.first + 1][current.second]
                    && mtx[current.first + 1][current.second] != 9
                    && !queue.contains(Pair(current.first + 1, current.second))
                ) {
                    queue.addLast(Pair(current.first + 1, current.second))
                }

                if (!visited[current.first - 1][current.second]
                    && mtx[current.first - 1][current.second] != 9
                    && !queue.contains(Pair(current.first - 1, current.second))
                ) {
                    queue.addLast(Pair(current.first - 1, current.second))
                }

                if (!visited[current.first][current.second - 1]
                    && mtx[current.first][current.second - 1] != 9
                    && !queue.contains(Pair(current.first, current.second - 1))
                ) {
                    queue.addLast(Pair(current.first, current.second - 1))
                }

                if (!visited[current.first][current.second + 1]
                    && mtx[current.first][current.second + 1] != 9
                    && !queue.contains(Pair(current.first, current.second + 1))
                ) {
                    queue.addLast(Pair(current.first, current.second + 1))
                }
            }

            basins.add(basinSize)
        }

        return basins.sortedDescending().subList(0, 3).reduce { acc, value -> acc * value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
