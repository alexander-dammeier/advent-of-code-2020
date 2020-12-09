package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.math.min

class Ex5 {

    @Test
    fun test() {
        val rows = 128
        val cols = 8
        val maxId = (javaClass.classLoader.getResource("ex5.txt")?.readText() ?: "")
            .lines()
            .asSequence()
            .map { it.substring(0, 8) to it.substring(7) }
            .map { (r, c) -> r.binarySeatSearch('F', rows) to c.binarySeatSearch('L', cols) }
            .map { (row, col) -> min(row.first, row.second) to min(col.first, col.second) }
            .map { (row, col) -> row * 8 + col }
            .max()
        assertEquals(908, maxId)
    }

    private fun String.binarySeatSearch(firstHalfIndicator: Char, max: Int) =
        this.fold(0 to max) { (start, end), rSide ->
            val half = (end - start) / 2;
            if (rSide == firstHalfIndicator) {
                start to (end - half)
            } else {
                (start + half) to end
            }
        }

    @Test
    fun test2() {
        val rows = 128
        val cols = 8
        val ids = (javaClass.classLoader.getResource("ex5.txt")?.readText() ?: "")
            .lines()
            .asSequence()
            .map { it.substring(0, 8) to it.substring(7) }
            .map { (r, c) -> r.binarySeatSearch('F', rows) to c.binarySeatSearch('L', cols) }
            .map { (row, col) -> min(row.first, row.second) to min(col.first, col.second) }
            .map { (row, col) -> row * 8 + col }

        val mySeatId = (0 until (rows * 8 + cols)).minus(ids)
            .zipWithNext()
            .filter { (id1, id2) -> id2 - id1 != 1 }
            .map { (id1, id2) -> max(id1, id2) }
            .first()
        assertEquals(619, mySeatId)
    }
}
