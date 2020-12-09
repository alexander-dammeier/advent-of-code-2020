package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ex9 {

    @Test
    fun test() {
        val windowSize = 26
        val number = (javaClass.classLoader.getResource("ex9.txt")?.readText() ?: "")
            .lines()
            .map { it.toLong() }
            .windowed(windowSize)
            .first { it.size == windowSize && !isSumOfAny(it[windowSize - 1], it.dropLast(1)) }[windowSize - 1]

        assertEquals(1639024365L, number)
    }

    private fun isSumOfAny(sum: Long, summands: List<Long>): Boolean {
        return sum == 0L || (sum > 0 && summands.any { it <= sum && isSumOfAny(sum - it, summands - it) })
    }

    @ExperimentalStdlibApi
    @Test
    fun test2() {
        val windowSize = 26
        val numbers = (javaClass.classLoader.getResource("ex9.txt")?.readText() ?: "")
            .lines()
            .map { it.toLong() }
        val number = numbers.windowed(windowSize)
            .first { it.size == windowSize && !isSumOfAny(it[windowSize - 1], it.dropLast(1)) }[windowSize - 1]

        assertEquals(1639024365L, number)

        val result = IntRange(2, numbers.size)
            .flatMap { size ->
                numbers.asSequence().windowed(size).map { it to it.sum() }.filter { (_, sum) -> sum == number }.toList()
            }
            .map { it.first }
            .first()
            .sorted()

        assertEquals(219202240L, result.first() + result.last())
    }
}
