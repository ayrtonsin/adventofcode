package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class InputDT(val grid: List<List<Char>>) {
    fun getScore(): Int {
        return 0
    }

}

fun main() {
    val day = 6
    println("Day $day part1 of Advent of Code 2024")
    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)// too low 5297
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(0)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")
}

private fun part2(day: Int) {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(0)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    return fileToInputDT(fileName).getScore()
}

private fun getScorePart2(fileName: String): Int {
    return fileToInputDT(fileName).getScore()
}

fun fileToInputDT(fileName: String): InputDT {
    val lines = File(fileName).readLines()
    return InputDT(lines.map {
        it.toList().map {
            when (it) {
//                '.' -> Tile.DOT
//                '#' -> Tile.HASH
            }
            it
        }
    })
}