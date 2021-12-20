fun main() {
    fun convert(config: List<Char>, image: Array<CharArray>, additionalPixel: Char): Array<CharArray> {
        val extendedImage = Array(image.size + 3 * 2) { i ->
            CharArray(image[0].size + 3 * 2) { j ->
                val out = i < 3 || i >= image.size + 3 || j < 3 || j >= image[0].size + 3
                if (out) additionalPixel else image[i - 3][j - 3]
            }
        }
        val result = Array(image.size + 3 * 2) {
            CharArray(image[0].size + 3 * 2) { additionalPixel }
        }
        for (i in 1 until extendedImage.size - 1) for (j in 1 until extendedImage[i].size - 1) {
            val binStr = extendedImage
                .slice(i - 1..i + 1)
                .joinToString(separator = "") {
                    it.slice(j - 1..j + 1).joinToString(separator = "")
                }
            val idx = binStr.toInt(2)
            result[i][j] = config[idx]
        }
        return result
    }

    fun convert(config: List<Char>, image: Array<CharArray>, k: Int): Array<CharArray> {
        var nextInput = image
        var additionalPixel = '0'
        repeat(k) {
            val iter = convert(config, nextInput, additionalPixel)
            additionalPixel = iter[1][1]
            nextInput = iter.slice(2..iter.lastIndex - 2)
                .map { it.sliceArray(2..it.lastIndex - 2) }
                .toTypedArray()
        }
        return nextInput
    }

    fun part1(input: List<String>): Int {
        val config = input[0].map { if (it == '#') '1' else '0' }
        val image = Array((input.size)) { CharArray(input[3].length) { '0' } }
        input.drop(2).forEachIndexed { i, s ->
            s.forEachIndexed { j, c -> image[i][j] = if (c == '#') '1' else '0' }
        }

        val result = convert(config, image, 2)
        return result.fold(0) { acc, line -> acc + line.count { it == '1' } }
    }

    fun part2(input: List<String>): Int {
        val config = input[0].map { if (it == '#') '1' else '0' }
        val image = Array((input.size)) { CharArray(input[3].length) { '0' } }
        input.drop(2).forEachIndexed { i, s ->
            s.forEachIndexed { j, c -> image[i][j] = if (c == '#') '1' else '0' }
        }

        val result = convert(config, image, 50)
        return result.fold(0) { acc, line -> acc + line.count { it == '1' } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
