package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File
import java.util.*


enum class Tile(val symbol: Char) {
    DOT('.'), HASH('#'), Cross('X'), START('^')

}

data class InputD6(val grid: List<List<Tile>>) {
//    fun plotRoute(): InputD6 {
//        val tiles = grid.toMutableList().map { it.toMutableList() }
//        val start = getStart()
//        //go north until you hit a wall then go right and repeat
//        var y = start.first
//        var x = start.second
//
//        while (!isOnEdge(y, x)) {
//
//            while (!isOnEdge(y, x) && tiles[y - 1][x] != Tile.HASH) {
//                y--
//                tiles[y][x] = Tile.Cross
//            }
//            if (isOnEdge(y, x)) {
//                break
//            }
//
//            while (!isOnEdge(y, x) && tiles[y][x + 1] != Tile.HASH) {
//                x++
//                tiles[y][x] = Tile.Cross
//            }
//            if (isOnEdge(y, x)) {
//                break
//            }
//
//            while (!isOnEdge(y, x) && tiles[y + 1][x] != Tile.HASH) {
//                y++
//                tiles[y][x] = Tile.Cross
//            }
//            if (isOnEdge(y, x)) {
//                break
//            }
//
//            while (!isOnEdge(y, x) && tiles[y][x - 1] != Tile.HASH) {
//                x--
//                tiles[y][x] = Tile.Cross
//            }
//        }
//        return InputD6(tiles)
//    }

    fun plotRoute(): Optional<InputD6> {
        val tiles = grid.map { it.toMutableList() }
        return plotRoute(tiles)
    }

    fun findInfiniteRoutesAddingSingleConstruction(): Int {

        var sum = 0
        for (y in 0 until grid.size) {
            for (x in 0 until grid[0].size) {
                val tiles = grid.map { it.toMutableList() }
                if (tiles[y][x] == Tile.DOT) {
                    tiles[y][x] = Tile.HASH
                    if (plotRoute(tiles).isEmpty) {
                        sum++
                    }

                }
            }
        }
        return sum
    }

    private fun plotRoute(tiles: List<MutableList<Tile>>): Optional<InputD6> {
        val start = getStart()
        val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
        var y = start.first
        var x = start.second
        var dirIndex = 0
        val visited = mutableSetOf<Triple<Int, Int, Int>>()

        while (!isOnEdge(y, x)) {
            if (!visited.add(Triple(y, x, dirIndex))) {
                println("Infinite loop detected at position ($y, $x) with direction $dirIndex")
                return Optional.empty()
            }
            val (dy, dx) = directions[dirIndex]
            if (isValidMove(y + dy, x + dx, tiles)) {
                y += dy
                x += dx
                tiles[y][x] = Tile.Cross
            } else {
                //change direction
                dirIndex = (dirIndex + 1) % 4
            }
        }
        return Optional.of(InputD6(tiles))
    }

    private fun isValidMove(y: Int, x: Int, tiles: List<MutableList<Tile>>): Boolean {
        return tiles[y][x] != Tile.HASH
    }


    private fun isOnEdge(y: Int, x: Int): Boolean {
        return y == 0 || x == 0 || y == grid.size - 1 || x == grid[0].size - 1
    }

    fun countCrosses(): Int {
        return grid.sumOf { row -> row.count { it == Tile.Cross } }
    }

    fun getStart(): Pair<Int, Int> {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == Tile.START) {
                    return Pair(row, col)
                }
            }
        }
        return Pair(-1, -1)
    }

    fun printGrid() {
        for (row in grid) {
            for (col in row) {
                print(
                    when (col) {
                        Tile.DOT -> '.'
                        Tile.HASH -> '#'
                        Tile.Cross -> 'X'
                        Tile.START -> '^'
                    }
                )
            }
            println()
        }
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
    assertThat(score).isEqualTo(41)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    //too low 5403
    println("result for input is: $score2")
}

private fun part2(day: Int) {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(6)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    return fileToInputD6(fileName).plotRoute().map { it.countCrosses() }.orElse(-1)
}

private fun getScorePart2(fileName: String): Int {
    return fileToInputD6(fileName).findInfiniteRoutesAddingSingleConstruction()
}

fun fileToInputD6(fileName: String): InputD6 {
    val lines = File(fileName).readLines()
    return InputD6(lines.map {
        it.toList().map {
            when (it) {
                '.' -> Tile.DOT
                '#' -> Tile.HASH
                '^' -> Tile.START
                else -> Tile.Cross
            }
        }
    })
}