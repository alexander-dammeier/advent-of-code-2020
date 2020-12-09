package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Ex1 {

    @Test
    fun test() {
        val ints = (javaClass.classLoader.getResource("ex1.txt")?.readText() ?: "")
                .lines()
                .map(String::toInt)
                .sorted()

        val (r1, r2, r3) = ints.reversed()
                .asSequence()
                .mapIndexed { index1, i1 ->
                    val search = ints.binarySearch(2020-i1, 0, ints.size - index1)
                    val end = if (search<0) search.absoluteValue-1 else search
                    ints
                            .subList(0, end)
                            .asSequence()
                            .mapIndexed { index2, i2 -> listOf(index2, i1, i2, 2020 - i1 - i2) }
                            .filter { (_, _, i2, i3) -> i3 in 0 until i2 }
                            .filter { (_, _, _, i3) -> ints.binarySearch(i3) >= 0}
                            .firstOrNull()?.let { (_, i1, i2, i3)-> listOf(Triple(i1, i2, i3)) } ?: listOf()
                }
                .flatten()
                .first()
        val result = r1 * r2 * r3
        assertEquals(161109702, result)
    }
}