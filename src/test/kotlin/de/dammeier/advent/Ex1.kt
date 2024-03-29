package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.absoluteValue

class Ex1 {

    @Test
    fun testPart1() {
        val ints = (javaClass.classLoader.getResource("ex1.txt")?.readText() ?: "")
            .lines()
            .map(String::toInt)
            .sorted()

        val intsReversed = ints.reversed()
        val (solution, _) = ints.fold((null to intsReversed) as Pair<Pair<Int, Int>?, List<Int>>) {
                (solution, rest), i ->
            if (solution != null) {
                solution to emptyList()
            } else {
                val neededSolution = 2020 - i
                val newRest = rest.dropWhile { it > neededSolution }
                val possibleSolution = newRest.firstOrNull()
                if (possibleSolution == neededSolution) {
                    i to possibleSolution to emptyList()
                } else {
                    null to newRest
                }
            }
        }
        assertNotNull(solution)
        if (solution != null) {
            assertEquals(2020, solution.first + solution.second)
            assertEquals(485739, solution.first * solution.second)
        }
    }

    @Test
    fun testPart2() {
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

    @Test
    fun generateTestData() {
        val ints = (javaClass.classLoader.getResource("ex1.txt")?.readText() ?: "")
            .lines()
            .map(String::toInt) + (2021 until 100_000)
        val shuffled = ints.shuffled()
        val file = Files.createFile(Path.of("generatedNumbers.txt"))
        Files.write(file, shuffled.map { it.toString() })
    }
}