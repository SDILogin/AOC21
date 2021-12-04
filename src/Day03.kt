private const val SLIDING_WINDOW_SIZE = 3

fun main() {
    fun part1(input: List<String>): Int {
        val arr = IntArray(input[0].length)
        input.forEach { binaryNumber ->
            binaryNumber.forEachIndexed { index, digit ->
                if (digit == '1') arr[index]++
            }
        }

        val threshold = input.size / 2
        val epsilonArr = arr.map { if (it < threshold) '1' else '0' }
        val gammaArr = epsilonArr.map { if (it == '1') '0' else '1' }

        val epsilon = Integer.parseInt(epsilonArr.joinToString(separator = ""), 2)
        val gamma = Integer.parseInt(gammaArr.joinToString(separator = ""), 2)
        return epsilon * gamma
    }

    fun part2(input: List<String>): Int {
        val oxygen = input.toMutableList()
        val co2 = input.toMutableList()
        for (bitPosition in 0 until input[0].length) {
            if (oxygen.size > 1) {
                val numberOfOnesInOxygen = oxygen.count { it[bitPosition] == '1' }
                val numberOfZerosInOxygen = oxygen.size - numberOfOnesInOxygen
                val targetBitValueInOxygen = if (numberOfOnesInOxygen >= numberOfZerosInOxygen) '1' else '0'
                oxygen.removeAll { it[bitPosition] != targetBitValueInOxygen }
            }

            if (co2.size > 1) {
                val numberOfOnesInCo2 = co2.count { it[bitPosition] == '1' }
                val numberOfZerosInCo2 = co2.size - numberOfOnesInCo2
                val targetBitValueInCo2 = if (numberOfZerosInCo2 <= numberOfOnesInCo2) '0' else '1'
                co2.removeAll { it[bitPosition] != targetBitValueInCo2 }
            }
        }

        val x1 = Integer.parseInt(oxygen[0], 2)
        val x2 = Integer.parseInt(co2[0], 2)
        return x1 * x2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
