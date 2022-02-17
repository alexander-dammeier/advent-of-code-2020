package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
            .minByOrNull { (_, inMinutes) -> inMinutes }!!
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
            .mapIndexed { index, s -> println(index to s);index to s }
            .filter { it.second != "x" }
            .map { it.first.toLong() to it.second.toLong() }
            .map { toRemainderAndLane(it.first, it.second) }
        val kgv = restAndLines
            .map { it.second }
            .reduce { acc, l -> acc * l }
        val chineseRestResult = restAndLines
            .map { Triple(it.first, it.second, kgv / it.second) }
            .map { (rest, a, m) ->
                val (one, s, t) = extendedEuclid(a, m)
                println("$s*$a + $t*$m = $one also e=${m * t}")
                m * t * rest
            }
            .sum()
        val result = Math.floorMod(chineseRestResult, kgv)
        assertEquals(780_601_154_795_940L, result)
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
        return Triple(ds, ts, ss - ts * (a / b))
    }

    private fun toRemainderAndLane(index: Long, lane: Long) =
        when {
            index == 0L -> Pair(index, lane)
            index < lane -> Pair(lane - index, lane)
            else -> Pair(lane - (index % lane), lane)
        }

}