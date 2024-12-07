package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File
import kotlin.math.abs

data class Report(val levels: List<Int>) {
    fun isSafe(): Boolean {
        return isSafe(levels)
    }

    fun isSafeByDampener(): Boolean {
        if (isSafe(levels)) {
            return true
        } else {
            for (i in 0 until levels.size) {
                val dampenedList = levels.toMutableList()
                dampenedList.removeAt(i)
                if (isSafe(dampenedList)) {
                    return true
                }
            }
            return false
        }
    }

    private fun isSafe(levels: List<Int>): Boolean {
        val safe = (allIncreasing(levels) || allDecreasing(levels))
                && gapLimitAtMostThree(levels)
        return safe
    }

    private fun gapLimitAtMostThree(levels: List<Int>): Boolean {
        for (i in 0 until levels.size - 1) {
            if (abs(levels[i + 1] - levels[i]) > 3) {
                return false
            }
        }
        return true

    }

    private fun allIncreasing(levels: List<Int>): Boolean {
        for (i in 0 until levels.size - 1) {
            if (levels[i] >= levels[i + 1]) {
                return false
            }
        }
        return true
    }

    private fun allDecreasing(levels: List<Int>): Boolean {
        for (i in 0 until levels.size - 1) {
            if (levels[i] <= levels[i + 1]) {
                return false
            }
        }
        return true
    }
}

fun main() {
    val day = "Day 2"
    println("$day part1 of Advent of Code 2024")
    part1()
    println("$day part2 of Advent of Code 2024")
    part2()
}

private fun part1() {
    val score = getScorePart1("src/main/resources/2024/SampleDay2.txt")
    assertThat(score).isEqualTo(2)

    val score2 = getScorePart1("src/main/resources/2024/InputDay2.txt")
    println("result for input is: $score2")
}

private fun part2() {
    val score = getScorePart2("src/main/resources/2024/SampleDay2.txt")
    assertThat(score).isEqualTo(4)

    val score2 = getScorePart2("src/main/resources/2024/InputDay2.txt")
    //693 is too low
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    val reports = fileToReports(fileName)
    return reports.filter { it.isSafe() }.size
}

private fun getScorePart2(fileName: String): Int {
    val reports = fileToReports(fileName)
    return reports.filter { it.isSafeByDampener() }.size
}


fun fileToReports(fileName: String): List<Report> {
    val lines = File(fileName).readLines()
    return lines.map { line ->
        val numbers = line.split(" ").map { it.toInt() }.toList()
        Report(numbers)
    }
}