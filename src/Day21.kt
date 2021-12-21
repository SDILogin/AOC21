import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        fun inc(x: Int, minX: Int, maxX: Int) = if (x + 1 > maxX) minX else x + 1

        var pos1 = input[0].substringAfter(": ").toInt()
        var pos2 = input[1].substringAfter(": ").toInt()

        var points1 = 0
        var points2 = 0
        var diceCounter = 0
        var dice = 0
        while (points2 < 1000) {
            repeat(3) {
                ++diceCounter
                dice = inc(dice, 1, 100)
                repeat(dice) {
                    pos1 = inc(pos1, 1, 10)
                }
            }
            points1 += pos1
            if (points1 >= 1000) break

            repeat(3) {
                ++diceCounter
                dice = inc(dice, 1, 100)
                repeat(dice) {
                    pos2 = inc(pos2, 1, 10)
                }
            }
            points2 += pos2
        }

        return diceCounter * min(points1, points2)
    }

    fun part2(input: List<String>): Long {
        val comb = mapOf(
            3 to 1,
            4 to 3,
            5 to 6,
            6 to 7,
            7 to 6,
            8 to 3,
            9 to 1
        )

        fun stat(
            gameState: GameState,
            memo: MutableMap<GameState, Pair<Long, Long>>
        ): Pair<Long, Long> {
            fun inRangeOf10(x: Int) = if (x % 10 == 0) 10 else x % 10

            val memorized = memo[gameState]
            if (memorized != null) return memorized

            if (gameState.firstPlayer.score >= 21) return Pair(1L, 0L)
            if (gameState.secondPlayer.score >= 21) return Pair(0L, 1L)

            val result = comb.map { entry ->
                val delta = entry.key
                val numberOfPaths = entry.value

                val player = if (gameState.firstPlayerMove) gameState.firstPlayer else gameState.secondPlayer
                val nextPosition = inRangeOf10(player.position + delta)
                val playerStateAfterMove = player.copy(
                    position = nextPosition,
                    score = player.score + nextPosition
                )
                val nextGameState = if (gameState.firstPlayerMove) {
                    gameState.copy(firstPlayerMove = false, firstPlayer = playerStateAfterMove)
                } else {
                    gameState.copy(firstPlayerMove = true, secondPlayer = playerStateAfterMove)
                }
                val nextGameStat = stat(nextGameState, memo)
                nextGameStat.copy(
                    first = nextGameStat.first * numberOfPaths,
                    second = nextGameStat.second * numberOfPaths
                )
            }

            return Pair(result.sumOf { it.first }, result.sumOf { it.second }).also {
                memo[gameState] = it
            }
        }

        val pos1 = input[0].substringAfter(": ").toInt()
        val pos2 = input[1].substringAfter(": ").toInt()

        val initialGameState = GameState(
            firstPlayer = Player(pos1, 0),
            secondPlayer = Player(pos2, 0),
            firstPlayerMove = true
        )
        val result = stat(initialGameState, mutableMapOf())
        return max(result.first, result.second)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

data class Player(
    val position: Int,
    val score: Int
)

data class GameState(
    val firstPlayer: Player,
    val secondPlayer: Player,
    val firstPlayerMove: Boolean
)
