fun main() {
    fun part1(input: List<String>): Int {
        fun neighbors(node: String, edges: List<Pair<String, String>>): List<String> {
            return edges
                .filter { it.first == node || it.second == node }
                .map { if (it.first == node) it.second else it.first }
        }

        fun walk(edges: List<Pair<String, String>>, node: String, visited: List<String>): List<List<String>> {
            if (node == "start") return listOf(visited + node)
            if (node == "end" && visited.isNotEmpty()) return emptyList()

            return neighbors(node, edges)
                .filter { neighbor -> neighbor.all { c -> c.isUpperCase() } || neighbor !in visited }
                .map { allowedToVisitNeighbor -> walk(edges, allowedToVisitNeighbor, visited + node) }
                .filter { it.isNotEmpty() }
                .flatten()
        }

        val edges = mutableListOf<Pair<String, String>>()
        input.forEach {
            val e = it.split('-')
            edges += Pair(e[0], e[1])
        }

        return walk(edges, "end", emptyList()).size
    }

    fun part2(input: List<String>): Int {
        fun neighbors(node: String, edges: List<Pair<String, String>>): List<String> {
            return edges
                .filter { it.first == node || it.second == node }
                .map { if (it.first == node) it.second else it.first }
        }

        fun canVisitAgain(visited: List<String>): Boolean {
            val visitedSmallCaves = visited.filter { it != "start" && it.all { c -> c.isLowerCase() } }
            return visitedSmallCaves.size == visitedSmallCaves.toSet().size
        }

        fun walk(edges: List<Pair<String, String>>, node: String, visited: List<String>): List<List<String>> {
            if (node == "start") return listOf(visited + node)
            if (node == "end" && visited.isNotEmpty()) return emptyList()

            return neighbors(node, edges)
                .filter { neighbor ->
                    neighbor.all { c -> c.isUpperCase() }
                            || neighbor !in visited
                            || canVisitAgain(visited + node)
                }
                .map { allowedToVisitNeighbor -> walk(edges, allowedToVisitNeighbor, visited + node) }
                .filter { it.isNotEmpty() }
                .flatten()
        }

        val edges = mutableListOf<Pair<String, String>>()
        input.forEach {
            val e = it.split('-')
            edges += Pair(e[0], e[1])
        }

        return walk(edges, "end", emptyList()).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
