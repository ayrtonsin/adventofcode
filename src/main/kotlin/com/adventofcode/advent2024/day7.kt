package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class Operation7(val expected: Int, val combinations: List<Int>){

    fun findValid(): Boolean {
        //find if doing a operator add or multiple in order of the list in combinations results to the expected value
        return findValid(combinations, expected);
    }

    fun findValid(combinations: List<Int>, expected: Int): Boolean {
        if (combinations.size == 1) {
            return combinations[0] == expected
        }
        val first = combinations[0]
        val rest = combinations.subList(1, combinations.size)
        return findValid(rest, expected - first) || findValid(rest, expected / first)
    }
}

data class InputD7(val operations: List<Operation7>) {
    fun result(): Int {
        return operations.filter { it.findValid() }.sumOf { it.expected }
    }
}

fun main() {
    val day = 7
    println("Day $day part1 of Advent of Code 2024")
    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)// too low 5297
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(3749)

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
    return fileToInputD7(fileName).result()
}

private fun getScorePart2(fileName: String): Int {
    return fileToInputD7(fileName).result()
}

fun fileToInputD7(fileName: String): InputD7 {
    val lines = File(fileName).readLines()
    val operations = lines.map {
        val parts = it.split(":")
        val expected = parts[0].toInt()
        val combinations = parts[1].trim().split(" ").map { it.toInt() }
        Operation7(expected, combinations)
    }
    return InputD7(operations)
}
