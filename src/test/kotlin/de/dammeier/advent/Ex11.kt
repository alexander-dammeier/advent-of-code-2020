package de.dammeier.advent

import de.dammeier.advent.Seat.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ex11 {

    @Test
    fun test() {
        val result = (javaClass.classLoader.getResource("ex11.txt")!!.readText())
            .lines()
            .toBoard()
            .tillNoChange(Board::evolve)
            .count(OCCUPIED)

        assertEquals(2093, result)
    }

    @Test
    fun test2() {
        val result = (javaClass.classLoader.getResource("ex11.txt")!!.readText())
            .lines()
            .toBoard()
            .tillNoChange(Board::evolve2)
            .count(OCCUPIED)
        assertEquals(1862, result)
    }
}

enum class Seat { FLOOR, EMPTY, OCCUPIED }
data class Board(val area: List<List<Seat>>) {

    private val xSize = area.size
    private val ySize = area.first().size

    fun get(x: Int, y: Int) = area.getOrNull(x)?.getOrNull(y) ?: FLOOR

    private fun getSeatsAround(x: Int, y: Int) = listOf(
        get(x - 1, y - 1), get(x, y - 1), get(x + 1, y - 1),
        get(x - 1, y), get(x + 1, y),
        get(x - 1, y + 1), get(x, y + 1), get(x + 1, y + 1)
    )

    private fun getFirstSeatInLines(x: Int, y: Int) = listOf(
        inDirection(x, y, -1, -1), inDirection(x, y, 0, -1), inDirection(x, y, 1, -1),
        inDirection(x, y, -1, 0), inDirection(x, y, 1, 0),
        inDirection(x, y, -1, 1), inDirection(x, y, 0, 1), inDirection(x, y, 1, 1)
    )

    private tailrec fun inDirection(x: Int, y: Int, xWay: Int, yWay: Int): Seat {
        val newX = x + xWay
        val newY = y + yWay
        return if (newX in 0 until xSize && y + yWay in 0 until ySize) {
            val seat = get(newX, newY)
            if (seat == FLOOR) inDirection(newX, newY, xWay, yWay)
            else seat
        } else {
            FLOOR
        }
    }

    fun tillNoChange(func: (Board) -> Board): Board = when (val evolved = func(this)) {
        this -> this
        else -> evolved.tillNoChange(func)
    }

    fun evolve() = Board(area.mapIndexed { x, row ->
        row.mapIndexed { y, _ -> evolveSeat(x, y) }
    })

    fun evolve2() = Board(area.mapIndexed { x, row ->
        row.mapIndexed { y, _ -> evolveSeat2(x, y) }
    })

    private fun evolveSeat(x: Int, y: Int): Seat {
        val seat = get(x, y)
        val occupiedAround = getSeatsAround(x, y).count { it == OCCUPIED }
        return when (seat) {
            FLOOR -> FLOOR
            EMPTY -> if (occupiedAround == 0) OCCUPIED else EMPTY
            OCCUPIED -> if (occupiedAround >= 4) EMPTY else OCCUPIED
        }
    }

    private fun evolveSeat2(x: Int, y: Int): Seat {
        val seat = get(x, y)
        val occupiedAround = getFirstSeatInLines(x, y).count { it == OCCUPIED }
        return when (seat) {
            FLOOR -> FLOOR
            EMPTY -> if (occupiedAround == 0) OCCUPIED else EMPTY
            OCCUPIED -> if (occupiedAround >= 5) EMPTY else OCCUPIED
        }
    }

    fun count(toCount: Seat) = area.flatten().count { it == toCount }

}

fun List<String>.toBoard() = Board(this.map { it.map { char -> char.parseSeat() } })

fun Char.parseSeat() = when (this) {
    '.' -> FLOOR
    'L' -> EMPTY
    '#' -> OCCUPIED
    else -> throw Exception("cannot parse Seat")
}