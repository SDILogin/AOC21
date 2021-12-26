fun main() {

    fun check(serialNumber: List<Int>, zReq: List<Int?>, toAdd: List<Int?>): List<Int>? {
        var z = 0
        val res = serialNumber.toMutableList()
        for (i in 0..serialNumber.lastIndex) {
            if (zReq[i] != null) {
                res[i] = (z % 26) + zReq[i]!!
                if (res[i] !in 1..9) return null
                z /= 26
            } else {
                z = 26 * z + serialNumber[i] + toAdd[i]!!
            }
        }
        return res
    }

    fun part1(input: List<String>): String {
        val programs = mutableListOf<MutableList<String>>()
        input.forEach { inst ->
            if (inst == "inp w") programs.add(mutableListOf()) else programs.last().add(inst)
        }

        val zReq = ArrayList<Int?>(14)
        val toAdd = ArrayList<Int?>(14)
        for (i in 0..input.lastIndex) {
            if (i % 18 == 4) {
                val down = input[i] == "div z 26"
                if (down) {
                    zReq.add(input[i + 1].split(' ')[2].toInt())
                    toAdd.add(null)
                } else {
                    zReq.add(null)
                    toAdd.add(input[i + 11].split(' ')[2].toInt())
                }
            }
        }

        val serialNumbers = mutableListOf<List<Int>>()
        for (x1 in 9 downTo 1) for (x2 in 9 downTo 1)
            for (x3 in 9 downTo 1) for (x4 in 9 downTo 1)
                for (x5 in 9 downTo 1) for (x6 in 9 downTo 1)
                    for (x7 in 9 downTo 1) {
                        var j = 0
                        val tmp = listOf(x1, x2, x3, x4, x5, x6, x7)
                        val serialNumber = MutableList(14) { i ->
                            toAdd[i]?.let { tmp[j++] } ?: 0
                        }
                        serialNumbers.add(serialNumber)
                    }

        val largestSerialNumber = serialNumbers.firstNotNullOf { check(it, zReq, toAdd) }
        return largestSerialNumber.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val programs = mutableListOf<MutableList<String>>()
        input.forEach { inst ->
            if (inst == "inp w") programs.add(mutableListOf()) else programs.last().add(inst)
        }

        val zReq = ArrayList<Int?>(14)
        val toAdd = ArrayList<Int?>(14)
        for (i in 0..input.lastIndex) {
            if (i % 18 == 4) {
                val down = input[i] == "div z 26"
                if (down) {
                    zReq.add(input[i + 1].split(' ')[2].toInt())
                    toAdd.add(null)
                } else {
                    zReq.add(null)
                    toAdd.add(input[i + 11].split(' ')[2].toInt())
                }
            }
        }

        val serialNumbers = mutableListOf<List<Int>>()
        for (x1 in 1..9) for (x2 in 1..9) for (x3 in 1..9)
            for (x4 in 1..9) for (x5 in 1..9) for (x6 in 1..9)
                for (x7 in 1..9) {
                    var j = 0
                    val tmp = listOf(x1, x2, x3, x4, x5, x6, x7)
                    val serialNumber = MutableList(14) { i ->
                        toAdd[i]?.let { tmp[j++] } ?: 0
                    }
                    serialNumbers.add(serialNumber)
                }

        val largestSerialNumber = serialNumbers.firstNotNullOf { check(it, zReq, toAdd) }
        return largestSerialNumber.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day24_test")
//    check(part1(testInput) == "13579246899999")

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
