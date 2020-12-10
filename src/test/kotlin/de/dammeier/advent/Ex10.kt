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
            .asSequence()
            .map { it.toLong() }
            .plus(0)
            .sorted()
            .windowed(2)
            .filter { it.size == 2 }
            .map { (i1, i2) -> i2 - i1 }
            .linearPermutations()

        assertEquals(12_089_663_946_752L, result)
    }

    private tailrec fun Sequence<Long>.linearPermutations(multiplyBy: Long = 1L): Long {
        if (this.none()) return multiplyBy
        val until3 = this.takeWhile { it != 3L }
        val size = until3.count()
        val newMultiplier = when (val str = until3.toList()) {
            listOf<Long>() -> 1
            listOf(1L) -> 1
            listOf(1L, 1L) -> 2
            listOf(1L, 1L, 1L) -> 4
            listOf(1L, 1L, 1L, 1L) -> 7
            else -> {
                println(str); 1
            }
        }
        return this.drop(size + 1).linearPermutations(newMultiplier * multiplyBy)
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