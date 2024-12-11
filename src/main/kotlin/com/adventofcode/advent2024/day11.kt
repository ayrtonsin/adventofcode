package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class InputD11(val stones: List<Long>) {
//    private val memo = mutableMapOf<Long, List<Long>>()

//    fun getScore(repeat: Int): Long {
//        var stonesM = stones
//        repeat(repeat) {
//            println("step $it stonesM.size = ${stonesM.size}")
//            stonesM = stonesM.flatMap { blink(it) }
//        }
//        return stonesM.size.toLong()
//    }
//
//    private fun blink(stone: Long): List<Long> {
//        return memo.getOrPut(stone) {
//            when {
//                stone == 0L -> listOf(1)
//                stone.toString().length % 2 == 0 -> {
//                    val firstPart = stone.toString().substring(0, stone.toString().length / 2).toLong()
//                    val secondPart = stone.toString().substring(stone.toString().length / 2).toLong()
//                    listOf(firstPart, secondPart)
//                }
//
//                else -> listOf(stone * 2024)
//            }
//        }
//    }

    private val memo = mutableMapOf<Pair<Long, Int>, List<Long>>()

    fun getScore(repeat: Int): Long {
        val result = stones.flatMap { processStone(it, repeat) }
        return result.size.toLong()
    }

    private fun processStone(stone: Long, repeat: Int): List<Long> {
        val key = Pair(stone, repeat)
        if (memo.containsKey(key)) return memo[key]!!

        if (repeat == 0) return listOf(stone)

        val result = when {
            stone == 0L -> listOf(1)
            stone.toString().length % 2 == 0 -> {
                val firstPart = stone.toString().substring(0, stone.toString().length / 2).toLong()
                val secondPart = stone.toString().substring(stone.toString().length / 2).toLong()
                listOf(firstPart, secondPart)
            }

            else -> listOf(stone * 2024)
        }.flatMap { processStone(it.toLong(), repeat - 1) }

        memo[key] = result
        return result
    }

}

fun main() {
    val day = 11
    println("Day $day part1 of Advent of Code 2024")
    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)// too low 5297
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(55312)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")
}

private fun part2(day: Int) {
//    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
//    assertThat(score).isEqualTo(0)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Long {
    return fileToInputD11(fileName).getScore(25)
}

private fun getScorePart2(fileName: String): Long {
    return fileToInputD11(fileName).getScore(75)
}

fun fileToInputD11(fileName: String): InputD11 {
    val stones = File(fileName).readLines().first().split(" ")
    return InputD11(stones.map { it.toLong() })

}