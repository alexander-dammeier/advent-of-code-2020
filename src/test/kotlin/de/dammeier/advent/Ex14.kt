package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@ExperimentalUnsignedTypes
class Ex14 {

    @Test
    fun test() {
        val result = (javaClass.classLoader.getResource("ex14.txt")!!.readText())
            .lines()
            .split { it.startsWith("mask") }
            .map(::parseGroups)
            //.map { println(it); it }
            .map { group ->
                group.assignments
                    .map { (i, v) -> i to group.mask.withMask(v) }
                    .toMap()
            }.fold(mapOf<ULong, ULong>()) { acc, map ->
                acc + map
            }.values.sum()

        assertTrue(6657481152L.toULong() < result, "$result is too low")
        assertTrue(9693190675322L.toULong() > result, "$result is too high")
        assertEquals(6559449933360L.toULong(), result)
    }

    @Test
    fun test2() {

    }

    private fun parseGroups(strings: List<String>): Group {
        val mask = parseMask(strings.first())
        val assignments = strings.drop(1).map(::parseAssignment)
        return Group(mask, assignments)
    }

    private fun parseMask(str: String): Mask {
        val maskStr = str.drop("mask = ".length)
        return maskStr.trim()
            .fold(0L.toULong() to 0L.toULong()) { (toOnes, toZeroes), char ->
                val newToOnes = toOnes shl 1
                val newToZeroes = toZeroes shl 1
                val one = 1L.toULong()
                when (char) {
                    '1' -> newToOnes + one to newToZeroes
                    '0' -> newToOnes to newToZeroes + one
                    else -> newToOnes to newToZeroes
                }
            }.let { Mask(it.first, it.second) }
    }

    private fun parseAssignment(str: String): Pair<ULong, ULong> {
        val index = str.dropWhile { it != '[' }
            .drop("[".length)
            .takeWhile { it.isDigit() }.toULong()
        val value = str.dropWhile { it != '=' }
            .drop("= ".length)
            .toULong()
        return index to value
    }

    data class Mask(val toOneMask: ULong, val toZeroMask: ULong) {
        fun withMask(value: ULong): ULong {
            return (value or toOneMask) and (toZeroMask.inv())
        }

        override fun toString(): String {
            return "Mask(toOneMask: ${toOneMask.toString(2)}" +
                    ", toZeroMask: ${toZeroMask.toString(2)})"
        }
    }


    data class Group(val mask: Mask, val assignments: List<Pair<ULong, ULong>>)

}