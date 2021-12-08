fun main() {
    fun part1(input: List<String>): Int {
        val uniqueLengths = listOf(2, 3, 4, 7)
        return input.fold(0) { acc, inputString ->
            acc + inputString.split('|')[1].split(' ')
                .count { it.length in uniqueLengths }
        }
    }

    /**
     *  0
     * 1 2
     *  3
     * 4 5
     *  6
     */
    fun part2(input: List<String>): Int {
        fun List<Set<Char>>.common(): Set<Char> {
            return this.reduce { common, chars -> common.intersect(chars) }
        }

        return input.fold(0) { acc, inputString ->
            val arr = inputString.split('|')

            val mapping = CharArray(7)
            val charArrays = arr[0].split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toSet() }

            val len2 = charArrays.first { it.size == 2 }
            val len3 = charArrays.first { it.size == 3 }
            val len4 = charArrays.first { it.size == 4 }
            val len5 = charArrays.filter { it.size == 5 }
            val len6 = charArrays.filter { it.size == 6 }
            val len7 = charArrays.first { it.size == 7 }

            mapping[0] = (len3 - len2).first() //
            mapping[3] = (len5.common() - len6.common()).first()
            mapping[1] = (len4 - len2 - mapping[3]).first()

            val x = len6.common() - mapping[1] - mapping[0]
            mapping[6] = (x - len2).first()
            mapping[2] = (len2 - x).first()
            mapping[5] = (len2 - mapping[2]).first()
            mapping[4] = (len7 - mapping[0] - mapping[1] - mapping[2] - mapping[3] - mapping[5] - mapping[6]).first()

            val setList = listOf(
                mapping.slice(listOf(0, 1, 2, 4, 5, 6)).toSet(),
                mapping.slice(listOf(2, 5)).toSet(),
                mapping.slice(listOf(0, 2, 3, 4, 6)).toSet(),
                mapping.slice(listOf(0, 2, 3, 5, 6)).toSet(),
                mapping.slice(listOf(1, 2, 3, 5)).toSet(),
                mapping.slice(listOf(0, 1, 3, 5, 6)).toSet(),
                mapping.slice(listOf(0, 1, 3, 4, 5, 6)).toSet(),
                mapping.slice(listOf(0, 2, 5)).toSet(),
                mapping.slice(listOf(0, 1, 2, 3, 4, 5, 6)).toSet(),
                mapping.slice(listOf(0, 1, 2, 3, 5, 6)).toSet()
            )

            val result = arr[1].split(' ').filter { it.isNotEmpty() }
                .fold(0) { output, input -> output * 10 + setList.indexOf(input.toSet()) }

            acc + result
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
