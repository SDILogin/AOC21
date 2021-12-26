fun main() {

    fun right(table: MutableList<MutableList<Char>>): Int {
        val n = table.size
        val m = table[0].size
        var res = 0
        val updated = table.map { it.map { it }.toMutableList() }.toMutableList()
        for (i in 0 until n) for (j in 0 until m) {
            if (table[i][j] == '>' && table[i][(j + 1) % m] == '.') {
                res++
                updated[i][(j + 1) % m] = '>'
                updated[i][j] = '.'
            }
        }
        for (i in 0 until n) for (j in 0 until m) table[i][j] = updated[i][j]
        return res
    }

    fun down(table: MutableList<MutableList<Char>>): Int {
        val n = table.size
        val m = table[0].size
        var res = 0
        val updated = table.map { it.map { it }.toMutableList() }.toMutableList()
        for (i in 0 until n) for (j in 0 until m) {
            if (table[i][j] == 'v' && table[(i + 1) % n][j] == '.') {
                res++
                updated[(i + 1) % n][j] = 'v'
                updated[i][j] = '.'
            }
        }
        for (i in 0 until n) for (j in 0 until m) table[i][j] = updated[i][j]
        return res
    }

    fun printTable(table: List<List<Char>>) {
        println(table.joinToString(separator = "\n") { it.joinToString(separator = "") })
        println()
    }

    fun part1(input: List<String>): Int {
        val table = MutableList(input.size) { i -> input[i].toCharArray().toList().toMutableList() }
        var counter = 1
        printTable(table)
        while (right(table) + down(table) > 0) {
            printTable(table)
            counter++
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
