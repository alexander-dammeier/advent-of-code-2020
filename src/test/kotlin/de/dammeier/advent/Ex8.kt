package de.dammeier.advent

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ex8 {
    interface Command
    data class ACC(val x: Int) : Command
    data class JMP(val x: Int) : Command
    data class NOP(val x: Int) : Command

    @Test
    fun test() {
        val commands = (javaClass.classLoader.getResource("ex8.txt")?.readText() ?: "")
            .lines()
            .map { it.parseCommand() }
        val result = execProgram(0, 0, commands, setOf())
        assertEquals(1134 to "loops", result)
    }

    private fun String.parseCommand(): Command {
        val name = this.take(3)
        val int = this.drop(4).toInt()
        return when (name) {
            "acc" -> ACC(int)
            "jmp" -> JMP(int)
            "nop" -> NOP(int)
            else -> throw IllegalArgumentException("$name is no valid command")
        }
    }

    private tailrec fun execProgram(
        cmdCounter: Int,
        acc: Int,
        commands: List<Command>,
        alreadyExecuted: Set<Int>
    ): Pair<Int, String> {
        if (cmdCounter !in commands.indices) {
            return acc to "terminated"
        }
        if (cmdCounter in alreadyExecuted) {
            return acc to "loops"
        }
        return when (val cmd = commands[cmdCounter]) {
            is ACC -> execProgram(cmdCounter + 1, acc + cmd.x, commands, alreadyExecuted + cmdCounter)
            is JMP -> execProgram(cmdCounter + cmd.x, acc, commands, alreadyExecuted + cmdCounter)
            is NOP -> execProgram(cmdCounter + 1, acc, commands, alreadyExecuted + cmdCounter)
            else -> throw IllegalArgumentException("$cmd is no valid command")
        }
    }

    @Test
    fun test2() {
        val commands = (javaClass.classLoader.getResource("ex8.txt")?.readText() ?: "")
            .lines()
            .map { it.parseCommand() }

        val jumps = commands.mapIndexed { i, cmd -> i to cmd }
            .filter { (_, cmd) -> cmd is JMP }
        val nops = commands.mapIndexed { i, cmd -> i to cmd }
            .filter { (_, cmd) -> cmd is NOP }
        val moddedPrograms = jumps.map { (i, cmd) ->
            commands.take(i) + NOP((cmd as JMP).x) + commands.drop(i + 1)
        } +
                nops.map { (i, cmd) ->
                    commands.take(i) + JMP((cmd as NOP).x) + commands.drop(i + 1)
                }
        val result = moddedPrograms.map { execProgram(0, 0, it, setOf()) }
            .first { (_, cause) -> cause == "terminated" }
            .first
        assertEquals(1205, result)
    }
}
