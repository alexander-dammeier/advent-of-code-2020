package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ex4 {

    @Test
    fun test() {
        val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid")
        val needed = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        val documents = (javaClass.classLoader.getResource("ex4.txt")?.readText() ?: "")
            .lines()
            .fold(listOf("")) { acc, s ->
                if (s.isBlank()) {
                    acc + s
                } else {
                    val last = acc.last()
                    acc.dropLast(1) + "$last $s"
                }
            }
            .map { str -> keys.map { key -> key to (str.contains("$key:")) } }
            .filter { list -> list.filter { it.second }.map { it.first }.containsAll(needed) }
            .count()

        assertEquals(233, documents)
    }

    @Test
    fun test2() {
        val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid")
        val needed = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        val documents = (javaClass.classLoader.getResource("ex4.txt")?.readText() ?: "")
            .lines()
            .fold(listOf("")) { acc, s ->
                if (s.isBlank()) {
                    acc + s
                } else {
                    val last = acc.last()
                    acc.dropLast(1) + "$last $s"
                }
            }
            .map { str -> str to keys.map { key -> key to (str.contains("$key:")) } }
            .filter { (_, list) -> list.filter { it.second }.map { it.first }.containsAll(needed) }
            .map { (str, keys) ->
                keys.map { (key, _) ->
                    val sub = str.substringAfter("$key:")
                    key to sub.takeWhile { !it.isWhitespace() }
                }.toMap()
            }
            .filter { map ->
                needed.filter { item ->
                    val str = (map[item] ?: "")
                    val valid = when (item) {
                        "byr" -> str.matches(Regex("\\d{4}")) && str.toInt() in 1920 until 2003
                        "iyr" -> str.matches(Regex("\\d{4}")) && str.toInt() in 2010 until 2021
                        "eyr" -> str.matches(Regex("\\d{4}")) && str.toInt() in 2020 until 2031
                        "hgt" -> str.matches(Regex("\\d{3}cm")) && str.take(3).toInt() in 150 until 194 ||
                                 str.matches(Regex("\\d{2}in")) && str.take(2).toInt() in 59 until 77
                        "hcl" -> str.matches(Regex("#[a-f0-9]{6}"))
                        "ecl" -> listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(str)
                        "pid" -> str.matches(Regex("\\d{9}"))
                        else -> true
                    }
                    valid
                }.count() == needed.count()
            }
            .count()

        assertEquals(111, documents)
    }
}
