package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ex7 {

    @Test
    fun test() {
        val rules = (javaClass.classLoader.getResource("ex7.txt")?.readText() ?: "")
            .lines()
            .map { it.parseRule() }
            .map { (k, v) -> k to v.toMap() }
            .toMap()
        val canContainIt = findContains("shiny gold", rules)
        assertEquals(254, canContainIt.count())
    }

    private fun findContains(bag: String, rules: Map<String, Map<String, Int>>): Set<String> {
        val containBag = rules.filter { (_, v) -> v.containsKey(bag) }
        val names = containBag.keys
        return names + names.flatMap { findContains(it, rules) }
    }

    private fun String.parseRule(): Pair<String, Set<Pair<String, Int>>> {
        val startMatcher = Regex(
            "(\\w+ \\w+) bag[s]* contain (\\d+|no) (other|\\w+ \\w+) bag[s]*(.*)\\."
        )
            .toPattern().matcher(this)
        if (!startMatcher.find()) {
            println("start not matching: $this")
        }
        val bagId = startMatcher.group(1) ?: ""
        val containsAmountStr = startMatcher.group(2)
        val containsAmount = if (containsAmountStr != "no") containsAmountStr.toInt() else 0
        val containsBagId = startMatcher.group(3) ?: ""
        val rest = startMatcher.group(4)
        return when {
            containsAmount == 0 -> bagId to setOf()
            rest.isEmpty() -> bagId to setOf(containsBagId to containsAmount)
            else -> bagId to setOf(containsBagId to containsAmount) + rest.parseAdditionalContents()
        }
    }

    private fun String.parseAdditionalContents(): Set<Pair<String, Int>> {
        val matcher = Regex(
            ", (\\d+) (\\w+ \\w+) bag[s]*(.*)"
        ).toPattern().matcher(this)

        if (!matcher.find()) {
            println("additional not matching: $this")
        }
        val containsAmount = (matcher.group(1) ?: "").toInt()
        val containsBagId = matcher.group(2)
        val rest = matcher.group(3)
        return setOf(containsBagId to containsAmount) +
                if (rest.isNotEmpty()) rest.parseAdditionalContents()
                else setOf()
    }

    @Test
    fun test2() {
        val rules = (javaClass.classLoader.getResource("ex7.txt")?.readText() ?: "")
            .lines()
            .map { it.parseRule() }
            .map { (k, v) -> k to v.toMap() }
            .toMap()
        val amount = hasToContain("shiny gold", rules)
        assertEquals(6006, amount)
    }

    fun hasToContain(bag: String, rules: Map<String, Map<String, Int>>): Long {
        return rules.getOrDefault(bag, mapOf())
            .map { (k, v) -> (1 + hasToContain(k, rules)) * v }
            .fold(0L) { i1, i2 -> i1 + i2 }
    }
}
