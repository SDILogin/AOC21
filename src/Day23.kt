import kotlin.math.absoluteValue

fun main() {
    val stepPrice = mapOf(
        'A' to 1,
        'B' to 10,
        'C' to 100,
        'D' to 1000
    )

    fun value(situation: Situation, memo: MutableMap<Situation, Long>): Long {
        if (memo.contains(situation)) return memo.getValue(situation)

        val allRoomsFull = situation.roomA.items.all { it == 'A' }
                && situation.roomB.items.all { it == 'B' }
                && situation.roomC.items.all { it == 'C' }
                && situation.roomD.items.all { it == 'D' }
        if (allRoomsFull) return 0

        fun checkPath(hall: String, hallIdx: Int, targetRoom: Room): Boolean {
            val roomIdx = targetRoom.positionInHall()
            return if (hallIdx < roomIdx) {
                hall.substring(hallIdx + 1, roomIdx).all { it == '.' }
            } else {
                hall.substring(roomIdx, hallIdx).all { it == '.' }
            }
        }

        var situationMin = INF
        val rooms = listOf(situation.roomA, situation.roomB, situation.roomC, situation.roomD)
        rooms.forEachIndexed { index, room ->
            if (room.isReadyForTarget()) {
                for (hallIdx in 0..situation.hall.lastIndex) {
                    if (situation.hall[hallIdx] != room.target) continue
                    if (checkPath(situation.hall, hallIdx, room)) {
                        val stepsInHall = (hallIdx - room.positionInHall()).absoluteValue
                        val stepsInRoom = room.items.count { it == '.' }
                        val pathPrice = (stepsInHall + stepsInRoom) * stepPrice.getValue(room.target)
                        val updatedHall = StringBuilder(situation.hall).apply { set(hallIdx, '.') }.toString()
                        val updatedRoom = room.push()
                        val nextSituation = when (index) {
                            0 -> situation.copy(hall = updatedHall, roomA = updatedRoom)
                            1 -> situation.copy(hall = updatedHall, roomB = updatedRoom)
                            2 -> situation.copy(hall = updatedHall, roomC = updatedRoom)
                            3 -> situation.copy(hall = updatedHall, roomD = updatedRoom)
                            else -> throw IllegalArgumentException()
                        }
                        val nextSituationMin = value(nextSituation, memo) + pathPrice
                        situationMin = minOf(nextSituationMin, situationMin)
                    }
                }

                val otherRooms = rooms - room
                val otherRoom = otherRooms.firstOrNull { otherRoom ->
                    otherRoom.items.firstOrNull { c -> c != '.' } == room.target
                }
                if (otherRoom != null && checkPath(situation.hall, otherRoom.positionInHall(), room)) {
                    val stepsInHall = (otherRoom.positionInHall() - room.positionInHall()).absoluteValue
                    val stepsInSourceRoom = otherRoom.items.count { it == '.' } + 1
                    val stepsInTargetRoom = room.items.count { it == '.' }
                    val steps = stepsInHall + stepsInSourceRoom + stepsInTargetRoom
                    val pathPrice = steps * stepPrice.getValue(room.target)
                    val updatedOtherRoom = otherRoom.pop()
                    val updatedRoom = room.push()
                    var nextSituation = when (rooms.indexOf(otherRoom)) {
                        0 -> situation.copy(roomA = updatedOtherRoom)
                        1 -> situation.copy(roomB = updatedOtherRoom)
                        2 -> situation.copy(roomC = updatedOtherRoom)
                        3 -> situation.copy(roomD = updatedOtherRoom)
                        else -> throw IllegalArgumentException()
                    }
                    nextSituation = when (index) {
                        0 -> nextSituation.copy(roomA = updatedRoom)
                        1 -> nextSituation.copy(roomB = updatedRoom)
                        2 -> nextSituation.copy(roomC = updatedRoom)
                        3 -> nextSituation.copy(roomD = updatedRoom)
                        else -> throw IllegalArgumentException()
                    }
                    val nextSituationMin = value(nextSituation, memo) + pathPrice
                    situationMin = minOf(nextSituationMin, situationMin)
                }
            }
        }

        rooms.forEachIndexed { index, room ->
            if (!room.isReadyForTarget()) {
                val c = room.items.first { it != '.' }
                val availablePositionsInHall = mutableListOf<Int>()
                val roomPositions = listOf(ROOM_A_POS, ROOM_B_POS, ROOM_C_POS, ROOM_D_POS)
                for (i in room.positionInHall() downTo 0) {
                    if (i in roomPositions) continue
                    if (situation.hall[i] == '.') availablePositionsInHall.add(i) else break
                }
                for (i in room.positionInHall()..situation.hall.lastIndex) {
                    if (i in roomPositions) continue
                    if (situation.hall[i] == '.') availablePositionsInHall.add(i) else break
                }
                availablePositionsInHall.forEach { targetPositionInHall ->
                    val stepsInHall = (targetPositionInHall - room.positionInHall()).absoluteValue
                    val stepsInRoom = room.items.count { it == '.' } + 1
                    val pathPrice = (stepsInHall + stepsInRoom) * stepPrice.getValue(c)
                    val updatedHall = StringBuilder(situation.hall).apply { set(targetPositionInHall, c) }.toString()
                    val updatedRoom = room.pop()
                    val nextSituation = when (index) {
                        0 -> situation.copy(hall = updatedHall, roomA = updatedRoom)
                        1 -> situation.copy(hall = updatedHall, roomB = updatedRoom)
                        2 -> situation.copy(hall = updatedHall, roomC = updatedRoom)
                        3 -> situation.copy(hall = updatedHall, roomD = updatedRoom)
                        else -> throw IllegalArgumentException()
                    }
                    val nextSituationMin = value(nextSituation, memo) + pathPrice
                    situationMin = minOf(nextSituationMin, situationMin)
                }
            }
        }

        memo[situation] = situationMin
        return situationMin
    }

    fun part1(input: List<String>): Long {
        val result = value(
            Situation(
                roomA = Room('A', listOf(input[2][3], input[3][3])),
                roomB = Room('B', listOf(input[2][5], input[3][5])),
                roomC = Room('C', listOf(input[2][7], input[3][7])),
                roomD = Room('D', listOf(input[2][9], input[3][9]))
            ),
            mutableMapOf()
        )

        return result
    }

    fun part2(input: List<String>): Long {
        return value(
            Situation(
                roomA = Room('A', listOf(input[2][3], 'D', 'D', input[3][3])),
                roomB = Room('B', listOf(input[2][5], 'C', 'B', input[3][5])),
                roomC = Room('C', listOf(input[2][7], 'B', 'A', input[3][7])),
                roomD = Room('D', listOf(input[2][9], 'A', 'C', input[3][9]))
            ),
            mutableMapOf()
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part2(testInput) == 44169L)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}

private const val INF = 1_000_000L
private const val ROOM_A_POS = 2
private const val ROOM_B_POS = 4
private const val ROOM_C_POS = 6
private const val ROOM_D_POS = 8

data class Room(val target: Char, val items: List<Char>) {
    fun isReadyForTarget() = items.all { it == '.' || it == target }
    fun push() = copy(
        items = items.toMutableList().apply {
            set(items.lastIndexOf('.'), target)
        }
    )

    fun pop() = copy(
        items = items.toMutableList().apply {
            set(items.indexOfFirst { it != '.' }, '.')
        }
    )

    fun positionInHall() = when (target) {
        'A' -> ROOM_A_POS
        'B' -> ROOM_B_POS
        'C' -> ROOM_C_POS
        'D' -> ROOM_D_POS
        else -> throw IllegalArgumentException()
    }
}

data class Situation(
    val hall: String = "...........",
    val roomA: Room,
    val roomB: Room,
    val roomC: Room,
    val roomD: Room
)
