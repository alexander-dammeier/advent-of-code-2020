package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor

class Ex13 {

    @Test
    fun test() {
        val (startTimeStr, busLinesStr) = (javaClass.classLoader.getResource("ex13.txt")!!.readText())
            .lines()
        val busLines = busLinesStr.split(',')
            .filter { it != "x" }
            .map { it.toInt() }
        val startTime = startTimeStr.toInt()
        val result = busLines.map { it to (it - (startTime % it)) % it }
            .minBy { (_, inMinutes) -> inMinutes }!!
            .let { println("$it");it.first * it.second }

        assertEquals(174, result)
    }

    @Test
    fun test2() {
        //https://de.wikipedia.org/wiki/Chinesischer_Restsatz
        val (_, busLinesStr) = (javaClass.classLoader.getResource("ex13.txt")!!.readText())
            .lines()
        val restAndLines = busLinesStr
            .split(',')
            .mapIndexed { index, s -> index to s }
            .filter { it.second != "x" }
            .map { it.first to it.second.toLong() }
        //val restAndLines = listOf(2L to 3L, 3L to 4L, 2L to 5L)
        val kgv = restAndLines
            .map { it.second }
            .reduce { acc, l -> acc * l }
        val es = restAndLines
            .map { Triple(it.first, it.second, kgv / it.second) } // (a,m)  m ist b in euklidischen Algo
            .map { (rest, a, m) ->
                val (one, s, t) = extendedEuclid(a, m)
                println("$s*$a + $t*$m = $one also e=${m*t}")
                m * t * rest
            }
        val result = Math.floorMod(es.sum(), kgv)
        assertTrue(100000000000000 < result, "$result is too low")
        assertTrue(1103346296257602 > result, "$result is too high")
        assertNotEquals(692754432903757, result)
        assertEquals(1_373_887_808_004_411L, result)
    }

    @Test
    fun testExtendedEuclid() {
        assertEquals(Triple(1L, 1L, 0L), extendedEuclid(1, 0))
        assertEquals(Triple(1L, 0L, 1L), extendedEuclid(2, 1))
        assertEquals(Triple(1L, 1L, -1L), extendedEuclid(3, 2))
        assertEquals(Triple(1L, -1L, 7L), extendedEuclid(20, 3))
        assertEquals(Triple(1L, 7L, -1L), extendedEuclid(3, 20))

        assertEquals(Triple(2L, 1L, 0L), extendedEuclid(2, 0))
        assertEquals(Triple(1L, -1L, 1L), extendedEuclid(2, 3))
    }

    private fun extendedEuclid(a: Long, b: Long): Triple<Long, Long, Long> {
        if (b == 0L) return Triple(a, 1, 0)
        val (ds, ss, ts) = extendedEuclid(b, Math.floorMod(a, b))
        return Triple(ds, ts, ss - ts * (a/b))
    }
}