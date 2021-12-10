fun main() {
    fun part1(input: List<String>): Int {
        fun closeBracket(char: Char) = when(char) {
            '(' -> ')'
            '{' -> '}'
            '[' -> ']'
            '<' -> '>'
            else -> throw IllegalArgumentException()
        }

        fun value(char: Char) = when(char) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }

        return input.fold(0) { acc, string ->
            val stack = ArrayDeque<Char>()
            string.toCharArray().forEach { bracket ->
                if (bracket == '(' || bracket == '{' || bracket == '[' || bracket == '<') {
                    stack.addLast(bracket)
                } else {
                    val openBracket = stack.removeLast()
                    val required = closeBracket(openBracket)
                    if (required != bracket) {
                        return@fold acc + value(bracket)
                    }
                }
            }
            acc
        }
    }

    fun part2(input: List<String>): Long {
        fun closeBracket(char: Char) = when(char) {
            '(' -> ')'
            '{' -> '}'
            '[' -> ']'
            '<' -> '>'
            else -> throw IllegalArgumentException()
        }

        fun score(char: Char) = when(char) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw IllegalArgumentException()
        }

        val scores = input.map { bracketsString ->
            val brackets = bracketsString.toCharArray()
            val stack = ArrayDeque<Char>()
            brackets.forEach { bracket ->
                if (bracket == '(' || bracket == '{' || bracket == '[' || bracket == '<') {
                    stack.addLast(bracket)
                } else if (closeBracket(stack.removeLast()) != bracket) {
                    return@map 0L
                }
            }

            var score = 0L
            while (stack.isNotEmpty()) {
                score = score * 5 + score(closeBracket(stack.removeLast()))
            }
            score
        }.filter { it > 0 }

        return scores.sorted()[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
