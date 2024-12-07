package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File

data class Memory(val input: String) {

    fun sanitise(): List<String> {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }

    fun sanitiseP2(): List<String> {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)|don't\(\)|do\(\)""".toRegex()
        return regex.findAll(input).map { it.value }.toList()
    }
}

data class Operation(val x: Int, val y: Int) {
    fun execute(): Int = x * y
}

fun main() {
    val day = "Day 3"
    println("$day part1 of Advent of Code 2024")
    part1()
    println("$day part2 of Advent of Code 2024")
    part2()
}

private fun part1() {
    val score = getScorePart1("src/main/resources/2024/SampleDay3.txt")
    assertThat(score).isEqualTo(161)

    val score2 = getScorePart1("src/main/resources/2024/InputDay3.txt")
    println("result for input is: $score2")//163931492
}

private fun part2() {
    val score = getScorePart2("src/main/resources/2024/SampleDay3p2.txt")
    assertThat(score).isEqualTo(48)

    val score2 = getScorePart2("src/main/resources/2024/InputDay3.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    val memory = fileToMemory(fileName).sanitise()
    return memory.stream()
        .map { fromString(it) }
        .map { it.execute() }
        .toList().sum()
}

private fun getScorePart2(fileName: String): Int {
    val memory = fileToMemory(fileName).sanitiseP2()
    var doIt = true
    var sum = 0
    for (i in memory) {
        if (i == "do()") {
            doIt = true
        } else if (i == "don't()") {
            doIt = false
        } else {
            if (doIt) {
                val operation = fromString(i)
                sum += operation.execute()
            }
        }
    }
    return sum
}

fun fromString(input: String): Operation {
    val first = input.substringAfter("mul(").substringBefore(",")
    val second = input.substringAfter(",").substringBefore(")")
    return Operation(first.toInt(), second.toInt())
}

fun fileToMemory(fileName: String): Memory {
    val lines = File(fileName).readLines()
    val combinedString = lines.joinToString(separator = "")
    return Memory(combinedString)
}