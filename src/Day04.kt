import java.lang.Integer.min

private const val SLIDING_WINDOW_SIZE = 3

fun main() {
    fun part1(input: List<String>): Int {
        val order = input[0].split(',').map { it.toInt() }
        val n = 5
        val tables = input.asSequence()
            .drop(1)
            .filter { it.isNotEmpty() }
            .chunked(n)
            .map { lines ->
                lines.map { line ->
                    line.split(' ').filter { it.isNotBlank() }.map { it.toInt() }
                }
            }
            .toList()

        var gameFinishAt = order.size
        var winnerIdx = -1
        tables.forEachIndexed { tableIdx, table ->
            val finishTableAt = List(table.size) { index ->
                val finishRowAtStep = table[index].maxOf { order.indexOf(it) }
                val finishColAtStep = table.map { it[index] }.maxOf { order.indexOf(it) }
                min(finishColAtStep, finishRowAtStep)
            }.minOf { it }

            if (finishTableAt < gameFinishAt) {
                winnerIdx = tableIdx
                gameFinishAt = finishTableAt
            }
        }

        val sumInTable = tables[winnerIdx].flatten().filter { order.indexOf(it) > gameFinishAt }.sum()
        return sumInTable * order[gameFinishAt]
    }

    fun part2(input: List<String>): Int {
        val order = input[0].split(',').map { it.toInt() }
        val n = 5
        val tables = input.asSequence()
            .drop(1)
            .filter { it.isNotEmpty() }
            .chunked(n)
            .map { lines ->
                lines.map { line ->
                    line.split(' ').filter { it.isNotBlank() }.map { it.toInt() }
                }
            }
            .toList()

        var gameFinishAt = -1
        var winnerIdx = -1
        tables.forEachIndexed { tableIdx, table ->
            val finishTableAt = List(table.size) { index ->
                val finishRowAtStep = table[index].maxOf { order.indexOf(it) }
                val finishColAtStep = table.map { it[index] }.maxOf { order.indexOf(it) }
                min(finishColAtStep, finishRowAtStep)
            }.minOf { it }

            if (finishTableAt > gameFinishAt) {
                winnerIdx = tableIdx
                gameFinishAt = finishTableAt
            }
        }

        val sumInTable = tables[winnerIdx].flatten().filter { order.indexOf(it) > gameFinishAt }.sum()
        return sumInTable * order[gameFinishAt]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
