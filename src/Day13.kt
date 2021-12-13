fun main() {
    fun part1(input: List<String>): Int {
        val dots = mutableListOf<Pair<Int, Int>>()
        val fold = mutableListOf<Pair<Char, Int>>()
        var positionsInput = true
        input.forEach {
            if (it.isEmpty()) {
                positionsInput = false
            } else if (positionsInput) {
                val arr = it.split(',').map { it.toInt() }
                dots.add(Pair(arr[0], arr[1]))
            } else if (it.startsWith("fold along y")) {
                fold.add(Pair('y', it.substringAfter("fold along y=").toInt()))
            } else if (it.startsWith("fold along x")) {
                fold.add(Pair('x', it.substringAfter("fold along x=").toInt()))
            }
        }

        val dim = fold[0].first
        val threshold = fold[0].second
        val mirrored = dots.map { dot ->
            if (dim == 'x' && dot.first > threshold) {
                dot.copy(first = 2 * threshold - dot.first)
            } else if (dim == 'y' && dot.second > threshold) {
                dot.copy(second = 2 * threshold - dot.second)
            } else dot
        }

        return mirrored.toSet().size
    }

    fun part2(input: List<String>): String {
        var dots = mutableListOf<Pair<Int, Int>>()
        val fold = mutableListOf<Pair<Char, Int>>()
        var positionsInput = true
        input.forEach {
            if (it.isEmpty()) {
                positionsInput = false
            } else if (positionsInput) {
                val arr = it.split(',').map { it.toInt() }
                dots.add(Pair(arr[0], arr[1]))
            } else if (it.startsWith("fold along y")) {
                fold.add(Pair('y', it.substringAfter("fold along y=").toInt()))
            } else if (it.startsWith("fold along x")) {
                fold.add(Pair('x', it.substringAfter("fold along x=").toInt()))
            }
        }

        fold.forEach {
            val dim = it.first
            val threshold = it.second
            dots = dots.map { dot ->
                if (dim == 'x' && dot.first > threshold) {
                    dot.copy(first = 2 * threshold - dot.first)
                } else if (dim == 'y' && dot.second > threshold) {
                    dot.copy(second = 2 * threshold - dot.second)
                } else dot
            }.toMutableList()
        }

        val n = dots.maxOf { it.first } + 1
        val m = dots.maxOf { it.second } + 1
        val mtx = Array(m) { CharArray(n) { ' ' } }
        dots.forEach { dot -> mtx[dot.second][dot.first] = '*' }
        return mtx.joinToString(separator = "\n") { it.concatToString() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
