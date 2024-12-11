package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class Coordinate(val x: Int, val y: Int)

data class InputD10(val grid: List<List<Int>>) {
    fun getScore(): Int {
        return grid.mapIndexed() { y, row ->
            row.mapIndexed { x, value ->
                if (value == 0) {
                    getAllHikingPaths(-1, Coordinate(x, y), mutableSetOf())
                } else {
                    0
                }
            }.sum()
        }.sum()
    }

    fun getAllHikingPaths(previous: Int, coordinate: Coordinate, visited: MutableSet<Coordinate>): Int {
        val x = coordinate.x
        val y = coordinate.y
        if (x < 0 || x >= grid[0].size || y < 0 || y >= grid.size || grid[y][x] - 1 != previous
//            || coordinate in visited
        ) {
            return 0
        }
//        visited.add(coordinate)
        val node = grid[y][x]
        if (node == 9) {
            return 1
        }
        var result = getAllHikingPaths(node, Coordinate(x - 1, y), visited)
        result += getAllHikingPaths(node, Coordinate(x + 1, y), visited)
        result += getAllHikingPaths(node, Coordinate(x, y - 1), visited)
        result += getAllHikingPaths(node, Coordinate(x, y + 1), visited)
//        visited.remove(coordinate)
        return result
    }

}

fun main() {
    val day = 10
//    println("Day $day part1 of Advent of Code 2024")
//    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)// too low 5297
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(36)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")
}

private fun part2(day: Int) {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(81)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    return fileToInputD10(fileName).getScore()
}

private fun getScorePart2(fileName: String): Int {
    return fileToInputD10(fileName).getScore()
}

fun fileToInputD10(fileName: String): InputD10 {
    val lines = File(fileName).readLines()
    return InputD10(lines.map {
        it.toList().map {
            it.toString().toInt()
        }
    })
}