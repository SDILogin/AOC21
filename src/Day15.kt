fun main() {
    fun part1(input: List<String>): Int {
        val inf = Integer.MAX_VALUE
        val mtx = Array(input.size + 2) { IntArray(input[0].length + 2) { inf } }
        input.map { it.toCharArray() }
            .forEachIndexed { i, chars -> chars.forEachIndexed { j, c -> mtx[i + 1][j + 1] = c.digitToInt() } }

        val unvisited = mutableSetOf<Pair<Int, Int>>()
        val dist = mutableMapOf<Pair<Int, Int>, Int>().apply { put(Pair(1, 1), 0) }
        mtx.forEachIndexed { i, ints -> ints.forEachIndexed { j, int -> if (int != inf) unvisited.add(Pair(i, j)) } }

        val minValue: (Map<Pair<Int, Int>, Int>) -> Pair<Int, Int> = { map ->
            var result = map.entries.first()
            map.forEach { entry ->
                if (entry.value < result.value) {
                    result = entry
                }
            }
            result.key
        }

        val target = Pair(input.size, input[0].length)
        while (unvisited.isNotEmpty()) {
            val current = minValue(dist.filterKeys { it in unvisited })
            if (current == target) break

            unvisited.remove(current)

            val unvisitedNeighbors = listOf(
                current.copy(first = current.first - 1),
                current.copy(first = current.first + 1),
                current.copy(second = current.second - 1),
                current.copy(second = current.second + 1),
            ).filter { it in unvisited }

            unvisitedNeighbors.forEach { neighbor ->
                val dst = dist.getValue(current) + mtx[neighbor.first][neighbor.second]
                if (dst < (dist[neighbor] ?: inf)) {
                    dist[neighbor] = dst
                }
            }
        }

        return dist.getValue(target)
    }

    fun part2(input: List<String>): Int {
        val inf = Integer.MAX_VALUE
        val n = input.size
        val m = input[0].length
        val mul = 5
        val mtx = Array(n * 5) { IntArray(m * 5) { 0 } }
        input.map { it.toCharArray() }
            .forEachIndexed { i, chars ->
                chars.forEachIndexed { j, c ->
                    mtx[i][j] = c.digitToInt()
                    for (k in m until m * mul step m) {
                        val x = mtx[i][k - m + j] + 1
                        mtx[i][j + k] = if (x == 10) 1 else x
                    }

                    for (t in n until n * mul step n) for (k in 0 until m * mul step m) {
                        val x = mtx[t - n + i][k + j] + 1
                        mtx[t + i][j + k] = if (x == 10) 1 else x
                    }
                }
            }

        val unvisited = mutableSetOf<Pair<Int, Int>>()
        val dist = mutableMapOf<Pair<Int, Int>, Int>().apply { put(Pair(0, 0), 0) }
        mtx.forEachIndexed { i, ints -> ints.forEachIndexed { j, _ -> unvisited.add(Pair(i, j)) } }

        val minValue: (Map<Pair<Int, Int>, Int>) -> Pair<Int, Int> = { map ->
            var result = map.entries.first()
            map.forEach { entry ->
                if (entry.value < result.value) {
                    result = entry
                }
            }
            result.key
        }

        val target = Pair(n * mul - 1, m * mul - 1)
        while (unvisited.isNotEmpty()) {
            val current = minValue(dist.filterKeys { it in unvisited })
            if (current == target) break

            unvisited.remove(current)

            val unvisitedNeighbors = listOf(
                current.copy(first = current.first - 1),
                current.copy(first = current.first + 1),
                current.copy(second = current.second - 1),
                current.copy(second = current.second + 1),
            ).filter { it in unvisited }

            unvisitedNeighbors.forEach { neighbor ->
                val dst = dist.getValue(current) + mtx[neighbor.first][neighbor.second]
                if (dst < (dist[neighbor] ?: inf)) {
                    dist[neighbor] = dst
                }
            }
        }

        return dist.getValue(target)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
