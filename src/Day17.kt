import java.lang.IllegalArgumentException

fun main() {
    fun part1(input: List<String>): Int {
        val arr = input[0].substringAfter("target area: x=").split(", y=")
        val xArr = arr[0].split("..").map { it.toInt() }
        val yArr = arr[1].split("..").map { it.toInt() }
        for (yv_init in 1000 downTo -1000) for (xv_init in 1..xArr[1]) {
            var yMax = 0
            var x = 0
            var y = 0
            var xv = xv_init
            var yv = yv_init
            while (x < xArr[1]) {
                x += xv
                y += yv
                --yv
                xv = maxOf(xv - 1, 0)
                yMax = maxOf(y, yMax)
                if (x >= xArr[0] && x <= xArr[1] && y >= yArr[0] && y <= yArr[1]) {
                    return yMax
                }
                if (x > xArr[1] || y < yArr[0] || (xv == 0 && x < xArr[0])) break
            }
        }
        throw IllegalArgumentException()
    }

    fun part2(input: List<String>): Int {
        val arr = input[0].substringAfter("target area: x=").split(", y=")
        val xArr = arr[0].split("..").map { it.toInt() }
        val yArr = arr[1].split("..").map { it.toInt() }
        var result = 0
        for (yv_init in 1000 downTo -1000) for (xv_init in 1..xArr[1]) {
            var x = 0
            var y = 0
            var xv = xv_init
            var yv = yv_init
            while (x < xArr[1]) {
                x += xv
                y += yv
                --yv
                xv = maxOf(xv - 1, 0)
                if (x >= xArr[0] && x <= xArr[1] && y >= yArr[0] && y <= yArr[1]) {
                    result++
                    break
                }
                if (x > xArr[1] || y < yArr[0] || (xv == 0 && x < xArr[0])) break
            }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
