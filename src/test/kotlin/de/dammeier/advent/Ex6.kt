package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ex6 {

    @Test
    fun test() {
        val sum = (javaClass.classLoader.getResource("ex6.txt")?.readText() ?: "")
            .trim()
            .lines()
            .map { it.trim() }
            .groupBySeparator("")
            .map { it.joinToString(separator = "") { str -> str } }
            .map { it.toSet().size }
            .sum()
        assertEquals(6763, sum)
    }

    fun <T> Iterable<T>.groupBySeparator(separator: T): List<List<T>> {
        val take = this.takeWhile { it != separator }
        val rest = this.drop(take.size + 1)
        return if (rest.isEmpty())
            listOf(take)
        else
            listOf(take) + rest.groupBySeparator(separator)
    }

    @Test
    fun test2() {
        val sum = (javaClass.classLoader.getResource("ex6.txt")?.readText() ?: "")
            .trim()
            .lines()
            .map { it.trim() }
            .groupBySeparator("")
            .map { group -> group.map { it.toSet() } }
            .map { group -> group.reduce { acc, set -> acc.intersect(set) } }
            .map { allYesPerGroup -> allYesPerGroup.count() }
            .sum()
        assertEquals(3512, sum)
    }
}
