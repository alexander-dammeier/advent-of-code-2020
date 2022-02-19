package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ex3 {

    @Test
    fun test() {
        val lines = (javaClass.classLoader.getResource("ex3.txt")?.readText() ?: "")
            .lines()
        val patternWidth = lines.first().length
        val toEncounter = lines.asSequence()
            .mapIndexed {index, line ->  index to line}
            .fold(0L) {acc, (i, line) ->
                val xPos = line[i*3 % (patternWidth)]
                if (xPos == '#') {
                    acc+1
                } else {
                    acc
                }
            }
        assertEquals(250, toEncounter)
    }

    @Test
    fun test2() {
        val stepSizes = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
        val lines = (javaClass.classLoader.getResource("ex3.txt")?.readText() ?: "")
            .lines()
        val patternWidth = lines.first().length
        val toEncounter = stepSizes.map { (x, y)->
            lines.asSequence()
                .mapIndexed {cy, line ->  cy to line}
                .filter { (cy, _) -> cy % y == 0}
                .mapIndexed {cx, (cy, line) -> Triple(cx, cy, line)}
                .fold(0L) {acc, (cx, _, line) ->
                    val xPos = line[cx*x % (patternWidth)]
                    if (xPos == '#') {
                        acc+1
                    } else {
                        acc
                    }
                }
            }
            .fold(1L) {acc, v -> acc*v }

        assertEquals(1592662500, toEncounter)
    }
}