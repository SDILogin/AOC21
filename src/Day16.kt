fun main() {
    val mapping = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    fun part1(input: List<String>): Int {
        fun parsePacket(bitsString: String, start: Int): Pair<Int, Int> {
            val version = bitsString.substring(start, start + 3).toInt(2)
            val type = bitsString.substring(start + 3, start + 6).toInt(2)
            when (type) {
                4 -> {
                    var i = start + 6
                    while (bitsString[i] == '1') i += 5
                    return Pair(i + 5, version)
                }
                else -> {
                    when (bitsString[start + 6]) {
                        '0' -> {
                            val len = bitsString.substring(start + 7, start + 22).toInt(2)
                            val end = start + 22 + len
                            var offset = start + 22
                            var res = 0
                            while (offset < end) {
                                val pack = parsePacket(bitsString, offset)
                                offset = pack.first
                                res += pack.second
                            }
                            return Pair(offset, version + res)
                        }
                        '1' -> {
                            val len = bitsString.substring(start + 7, start + 18).toInt(2)
                            var res = 0
                            var offset = start + 18
                            repeat(len) {
                                val pack1 = parsePacket(bitsString, offset)
                                offset = pack1.first
                                res += pack1.second
                            }
                            return Pair(offset, res + version)
                        }
                        else -> throw IllegalStateException()
                    }
                }
            }
        }

        return parsePacket(input[0].map { mapping[it] }.joinToString(separator = ""), 0).second
    }

    fun part2(input: List<String>): Long {
        fun applyOp(opCode: Int, params: List<Pair<Int, Long>>): Long {
            val numbers = params.map { it.second }
            return when (opCode) {
                0 -> numbers.sum()
                1 -> numbers.fold(1) { acc, i -> acc * i }
                2 -> numbers.minOf { it }
                3 -> numbers.maxOf { it }
                5 -> if (numbers[0] > numbers[1]) 1 else 0
                6 -> if (numbers[0] < numbers[1]) 1 else 0
                7 -> if (numbers[0] == numbers[1]) 1 else 0
                else -> throw IllegalArgumentException()
            }
        }

        fun parsePacket(bitsString: String, start: Int): Pair<Int, Long> {
            val opCode = bitsString.substring(start + 3, start + 6).toInt(2)
            when (opCode) {
                4 -> {
                    var i = start + 6
                    var valueStr = ""
                    while (bitsString[i] == '1') {
                        valueStr += bitsString.substring(i + 1, i + 5)
                        i += 5
                    }
                    valueStr += bitsString.substring(i + 1, i + 5)
                    return Pair(i + 5, valueStr.toLong(2))
                }
                else -> {
                    when (bitsString[start + 6]) {
                        '0' -> {
                            val len = bitsString.substring(start + 7, start + 22).toInt(2)
                            val end = start + 22 + len
                            var offset = start + 22
                            val evaluatedParams = mutableListOf<Pair<Int, Long>>()
                            while (offset < end) {
                                val pack = parsePacket(bitsString, offset)
                                evaluatedParams.add(pack)
                                offset = pack.first
                            }
                            val res = applyOp(opCode, evaluatedParams)
                            return Pair(offset, res)
                        }
                        '1' -> {
                            val len = bitsString.substring(start + 7, start + 18).toInt(2)
                            var offset = start + 18
                            val evaluatedParams = mutableListOf<Pair<Int, Long>>()
                            repeat(len) {
                                val pack = parsePacket(bitsString, offset)
                                evaluatedParams.add(pack)
                                offset = pack.first
                            }
                            val res = applyOp(opCode, evaluatedParams)
                            return Pair(offset, res)
                        }
                        else -> throw IllegalStateException()
                    }
                }
            }
        }

        return parsePacket(input[0].map { mapping[it] }.joinToString(separator = ""), 0).second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part2(testInput) == 1L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
