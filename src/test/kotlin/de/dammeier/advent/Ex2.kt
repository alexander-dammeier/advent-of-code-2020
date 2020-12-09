package de.dammeier.advent

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ex2 {

    @Test
    fun test() {
        val pws = (javaClass.classLoader.getResource("ex2.txt")?.readText() ?: "")
                .lines()
                .map {
                    val regex = Regex("(\\d*)-(\\d*) (\\w): (\\w*)")
                    val matcher = regex.toPattern().matcher(it)
                    if(!matcher.find()) {
                        println("not matching: $it")
                    }
                    val min = matcher.group(1)
                    val max = matcher.group(2)
                    val letter = matcher.group(3)
                    val word = matcher.group(4)
                    listOf(min, max, letter, word)
                }
                .filter { (min, max, letter, word) ->
                    val amount = word.filter { it == letter.first() }.count()
                    amount in min.toInt() until max.toInt()+1
                }
                .count()
        assertEquals(536, pws)
    }

    @Test
    fun testPart2() {
        val pws = (javaClass.classLoader.getResource("ex2.txt")?.readText() ?: "")
                .lines()
                .map {
                    val regex = Regex("(\\d*)-(\\d*) (\\w): (\\w*)")
                    val matcher = regex.toPattern().matcher(it)
                    if(!matcher.find()) {
                        println("not matching: $it")
                    }
                    val min = matcher.group(1)
                    val max = matcher.group(2)
                    val letter = matcher.group(3)
                    val word = matcher.group(4)
                    listOf(min, max, letter, word)
                }
                .filter { (min, max, letter, word) ->
                    word.length >= max.toInt() &&
                            ((word[min.toInt()-1]==letter.first()) xor
                            (word[max.toInt()-1]==letter.first()))
                }
                .count()
        assertEquals(558, pws)
    }
}