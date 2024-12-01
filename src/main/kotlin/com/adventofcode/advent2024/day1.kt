package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File
import kotlin.math.abs


data class LocationLists(val list1: List<Int>, val list2: List<Int>)

fun main() {
    val day = "Day 3"
    println("$day part1 of Advent of Code 2022")
    part1()
    println("$day part2 of Advent of Code 2022")
    part2()
}

private fun part1() {
    val score = getScorePart1("src/main/resources/2024/inputSampleDay1-1.txt")
    assertThat(score).isEqualTo(11)

    val score2 = getScorePart1("src/main/resources/2024/inputDay1.txt")
    println("result for input is: $score2")
}

private fun part2() {
    val score = getScorePart2("src/main/resources/2024/inputSampleDay1-1.txt")
    assertThat(score).isEqualTo(31)

    val score2 = getScorePart2("src/main/resources/2024/inputDay1.txt")
    println("result for input is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    val locations = fileToDoubleList(fileName);
    val list1 = locations.list1.sorted();
    val list2 = locations.list2.sorted();

    return list1.zip(list2).sumOf {
        abs(it.first - it.second)
    }
}

private fun getScorePart2(fileName: String): Int {
    val locations = fileToDoubleList(fileName);
    val list1 = locations.list1.sorted();
    val list2 = locations.list2.sorted();
    val groupedList2 =  list2.groupBy({ it },{ it }).mapValues { it.key * it.value.size }

    return list1.sumOf { groupedList2[it] ?: 0}
}


fun fileToDoubleList(fileName: String): LocationLists {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    val lines = File(fileName).readLines()
    lines.map { line ->
        val split = line.split("   ")
        list1.add(split[0].toInt())
        list2.add(split[1].toInt())
    }
    return LocationLists(list1, list2)
}