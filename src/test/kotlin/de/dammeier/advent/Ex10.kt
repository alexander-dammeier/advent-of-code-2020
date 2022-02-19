package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ex10 {

    @Test
    fun test() {
        val result = (javaClass.classLoader.getResource("ex10.txt")!!.readText())
            .lines()
            .map { it.toLong() }
            .sorted()
            .fold(Triple(0L, 0L, 1L)) { (prev, oneCount, threeCount), current ->
                when (current - prev) {
                    1L -> Triple(current, oneCount + 1, threeCount)
                    3L -> Triple(current, oneCount, threeCount + 1)
                    else -> Triple(current, oneCount, threeCount)
                }
            }

        assertEquals(2312, result.second * result.third)
    }

    @Test
    fun test2() {
        val result = (javaClass.classLoader.getResource("ex10.txt")!!.readText())
            .lines()
            .map { it.toLong() }
            .plus(0)
            .sorted()
            .windowed(2, partialWindows = false)
            .map { (i1, i2) -> i2 - i1 }
            .split { it == 3L }
            .map { it.filter { i-> i != 3L } }
            .map { group->
                when (group) {
                    listOf<Long>() -> 1L
                    listOf(1L) -> 1L
                    listOf(1L, 1L) -> 2L
                    listOf(1L, 1L, 1L) -> 4L
                    listOf(1L, 1L, 1L, 1L) -> 7L
                    else -> {
                        println(group); 1L
                    }
                }
            }
            .fold(1L) {acc, i -> acc*i }

        assertEquals(12_089_663_946_752L, result)
    }

}

/*
 1  1  3 = *2
0, 1, 2, 5
0, 2, 5


 1  1  1  3 = *4
0, 1, 2, 3, 6
0, 1, 3, 6
0, 2, 3, 6
0, 3, 6

 1  1  1  1  3 = *7
0, 1, 2, 3, 4, 7
0, 1, 2, 4, 7
0, 1, 3, 4, 7
0, 1, 4, 7
0, 2, 3, 4, 7
0, 2, 4, 7
0, 3, 4, 7
 */