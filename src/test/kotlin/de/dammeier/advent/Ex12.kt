package de.dammeier.advent

import de.dammeier.advent.InstructionType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Ex12 {

    @Test
    fun test() {
        val result = (javaClass.classLoader.getResource("ex12.txt")!!.readText())
            .lines()
            .map(::parseCommand)
            .fold(Ship(0, 0)) { ship, command -> ship.execCommand(command) }
            .let { it.x.absoluteValue + it.y.absoluteValue }

        assertEquals(938, result)
    }

    @Test
    fun test2() {
        val result = (javaClass.classLoader.getResource("ex12.txt")!!.readText())
            .lines()
            .map(::parseCommand)
            .flatMap {
                if ((it.instruction == R || it.instruction == L) && it.amount > 90) {
                    val steps = it.amount / 90
                    (0 until steps).map { _ -> Command(it.instruction, 90) }.toList()
                } else
                    listOf(it)
            }
            .fold(Waypoint(10, 1, 0, 0)) { wayPoint, command -> wayPoint.execCommand(command) }
            .let { it.shipX.absoluteValue + it.shipY.absoluteValue }
        assertEquals(54404, result)
    }
}

fun parseCommand(str: String): Command {
    val instr = InstructionType.valueOf(str.take(1))
    val amount = str.drop(1).toInt()
    return Command(instr, amount)
}

data class Command(val instruction: InstructionType, val amount: Int)
enum class InstructionType { N, S, E, W, F, L, R }
enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun toCommand(amount: Int) = when (this) {
        NORTH -> Command(N, amount)
        EAST -> Command(E, amount)
        SOUTH -> Command(S, amount)
        WEST -> Command(W, amount)
    }

    fun rotate(degrees: Int): Direction {
        val steps = degrees / 90
        val values = enumValues<Direction>()
        val nextOrdinal = Math.floorMod(ordinal + steps, values.size)
        return values[nextOrdinal]
    }
}


data class Waypoint(val x: Int, val y: Int, val shipX: Int, val shipY: Int) {
    fun execCommand(cmd: Command) = when (cmd.instruction) {
        N -> this.copy(y = y + cmd.amount)
        S -> this.copy(y = y - cmd.amount)
        E -> this.copy(x = x + cmd.amount)
        W -> this.copy(x = x - cmd.amount)
        F -> this.copy(shipX = shipX + x * cmd.amount, shipY = shipY + y * cmd.amount)
        L -> this.copy(x = -y, y = x)
        R -> this.copy(x = y, y = -x)
    }
}

data class Ship(val x: Int, val y: Int, val facing: Direction = Direction.EAST) {
    tailrec fun execCommand(cmd: Command): Ship {
        return when (cmd.instruction) {
            N -> this.copy(y = y + cmd.amount)
            S -> this.copy(y = y - cmd.amount)
            E -> this.copy(x = x + cmd.amount)
            W -> this.copy(x = x - cmd.amount)
            F -> execCommand(facing.toCommand(cmd.amount))
            L -> this.copy(facing = facing.rotate(-cmd.amount))
            R -> this.copy(facing = facing.rotate(cmd.amount))
        }
    }
}