fun main() {
    fun part1(input: List<String>, steps: Int = 10): Int {
        val base = input[0]
        val mapping = input.subList(2, input.size).associate {
            val arr = it.split(" -> ")
            Pair(arr[0], arr[1])
        }
        val sb = StringBuilder(base)
        repeat(steps) {
            val pairs = sb.zipWithNext().map { "${it.first}${it.second}" }
            val insertTable = pairs
                .mapIndexed { index, s -> mapping[s]?.let { c -> Pair(index + 1, c) } }
                .filterNotNull()
            insertTable.asReversed().forEach { ins -> sb.insert(ins.first, ins.second) }
        }
        val charGroups = sb.groupBy { it }
        val max = charGroups.maxOf { it.value.size }
        val min = charGroups.minOf { it.value.size }
        return max - min
    }

    fun part2(input: List<String>, steps: Int = 40): Long {
        val allCombinations = mutableMapOf<String, Long>()

        val mapping = input.subList(2, input.size).associate {
            val arr = it.split(" -> ")
            allCombinations["${arr[0][0]}${arr[1]}"] = 0L
            allCombinations["${arr[0][1]}${arr[1]}"] = 0L
            Pair(arr[0], arr[1])
        }

        val base = input[0]
        base.zipWithNext()
            .map { "${it.first}${it.second}" }
            .forEach { allCombinations[it] = (allCombinations[it] ?: 0) + 1L }

        val freq: MutableMap<Char, Long> = ('A'..'Z')
            .associateWith { c -> base.count { it == c }.toLong() }
            .toMutableMap()

        repeat(steps) {
            val currentComb = allCombinations.filterValues { it > 0 }
            allCombinations.forEach { entry -> allCombinations[entry.key] = 0L }
            currentComb.forEach { entry ->
                mapping[entry.key]?.let { c ->
                    val x1 = allCombinations["${entry.key[0]}$c"] ?: 0L
                    val x2 = allCombinations["$c${entry.key[1]}"] ?: 0L
                    allCombinations["${entry.key[0]}$c"] = x1 + entry.value
                    allCombinations["$c${entry.key[1]}"] = x2 + entry.value

                    val x3 = freq[c[0]] ?: 0L
                    freq[c[0]] = x3 + entry.value
                }
            }
        }

        with(freq.filterValues { it > 0 }) {
            return maxOf { it.value } - minOf { it.value }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part2(testInput, 40) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
