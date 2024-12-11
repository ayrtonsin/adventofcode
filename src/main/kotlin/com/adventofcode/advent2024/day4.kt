package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File

val day = 4
val xmas = "XMAS"
val xmas_INV = "SAMX"

data class Puzzle(val puzzle: List<List<Char>>) {

    //part 2
    fun countCrosses(): Int {
        var sum = 0
        for (row in 0 until puzzle.size - 2) {
            for (x in 0 until puzzle[0].size - 2) {
                //ensure middle is an A
                if (puzzle[row + 1][x + 1] != 'A') {
                    continue
                }
                //ensure diagonals have an M and an S
                if (!(puzzle[row][x] == 'M' && puzzle[row + 2][x + 2] == 'S')
                    && !(puzzle[row][x] == 'S' && puzzle[row + 2][x + 2] == 'M')
                ) {
                    continue
                }
                if (!(puzzle[row + 2][x] == 'M' && puzzle[row][x + 2] == 'S')
                    && !(puzzle[row + 2][x] == 'S' && puzzle[row][x + 2] == 'M')
                ) {
                    continue
                }
                sum++
            }
        }
        return sum;
    }

    //part 1
    fun countXMasses(): Int {
        return countOccurrences(xmas) + countOccurrences(xmas_INV)
    }

    private fun countOccurrences(pattern: String): Int {
        return countHorizontalOccurrences(pattern) + countVerticalOccurrences(pattern) + countDiagonalOccurrences(pattern)
    }

    private fun countHorizontalOccurrences(pattern: String): Int {
        var count = 0
        for (row in puzzle) {
            val rowString = row.joinToString("")
            count += rowString.windowed(pattern.length).count { it == pattern }
        }
        return count
    }

    private fun countVerticalOccurrences(pattern: String): Int {
        var count = 0
        for (col in 0 until puzzle[0].size) {
            val colString = puzzle.map { it[col] }.joinToString("")
            count += colString.windowed(pattern.length).count { it == pattern }
        }
        return count
    }

    private fun countDiagonalOccurrences(pattern: String): Int {
        return countTopLeftToBottomRight(pattern) + countBottomLeftToTopRight(pattern)
    }

    private fun countBottomLeftToTopRight(pattern: String): Int {
        var count = 0
        val n = puzzle.size
        val m = puzzle[0].size

        for (k in 0 until n + m - 1) {
            val diagonal = mutableListOf<Char>()
            for (j in 0..k) {
                val i = n - 1 - k + j
                if (i >= 0 && i < n && j < m) {
                    diagonal.add(puzzle[i][j])
                }
            }
            val diagonalString = diagonal.joinToString("")
            count += diagonalString.windowed(pattern.length).count { it == pattern }
        }
        return count
    }

    private fun countTopLeftToBottomRight(pattern: String): Int {
        var count = 0
        val n = puzzle.size
        val m = puzzle[0].size

        for (k in 0 until n + m - 1) {
            val diagonal = mutableListOf<Char>()
            for (j in 0..k) {
                val i = k - j
                if (i < n && j < m) {
                    diagonal.add(puzzle[i][j])
                }
            }
            val diagonalString = diagonal.joinToString("")
            count += diagonalString.windowed(pattern.length).count { it == pattern }
        }
        return count
    }

}

fun main() {
    val day = "Day " + day
    println("$day part1 of Advent of Code 2024")
    part1()
    println("$day part2 of Advent of Code 2024")
    part2()
}

private fun part1() {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(18)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")
}

private fun part2() {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(9)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    return fileToPuzzle(fileName).countXMasses();
}

private fun getScorePart2(fileName: String): Int {
    return fileToPuzzle(fileName).countCrosses();
}

fun fileToPuzzle(fileName: String): Puzzle {
    val lines = File(fileName).readLines()
    return Puzzle(lines.map { it.toList() })
}