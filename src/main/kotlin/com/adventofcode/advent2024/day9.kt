package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class Part(val empty: Boolean, val index: Long)


class InputD9(val parts: List<Part>) {
    fun getScore(): Long {
        val newParts = reorderParts()

        return newParts
            .filter { !it.empty }
            .mapIndexed { index, part ->
                index * part.index
            }.sum()
    }

    private fun reorderParts(): MutableList<Part> {
        val newParts = parts.toMutableList()
        var i = -1
        var y = parts.size
        var firstEmpty: Part
        var lastNonEmpty: Part
        do {
            i++
            y--
            firstEmpty = parts[i]
            lastNonEmpty = parts[y]
            while (!firstEmpty.empty && i < y) {
                i++
                if (i < parts.size) {
                    firstEmpty = parts[i]
                }
            }

            if (!firstEmpty.empty) {
                break
            }

            while (lastNonEmpty.empty && y > i) {
                y--
                lastNonEmpty = parts[y]
            }

            if (i < y) {
                newParts[i] = lastNonEmpty
                newParts[y] = firstEmpty
            }

        } while (i < y)
        return newParts
    }

    fun getScoreV2(): Long {
        val newParts = reorderPartsV2()

        var sum = 0L
        for (i in newParts.indices) {
            if (!newParts[i].empty)
                sum += i * newParts[i].index
        }
        return sum
    }

    private fun reorderPartsV2(): List<Part> {
        val newParts = parts.toMutableList()
        var y = parts.size - 1

        while (y >= 0) {
            if (parts[y].empty) {
                y--
            } else {
                val index = parts[y].index
                var blockStart = y
                while (blockStart >= 0 && parts[blockStart].index == index && !parts[blockStart].empty) {
                    blockStart--
                }
                blockStart++

                val blockSize = y - blockStart + 1
                val leftMostFreeBlockIdx = leftMostIndexFreeBlock(newParts, blockSize, blockStart)
                if (leftMostFreeBlockIdx != -1) {
                    for (i in 0 until blockSize) {
                        newParts[leftMostFreeBlockIdx + i] = parts[blockStart + i]
                        newParts[blockStart + i] = Part(true, 0)
                    }
                }
                y = blockStart - 1
            }
        }
        return newParts.toList()
    }

    private fun leftMostIndexFreeBlock(newParts: List<Part>, blockSize: Int, y: Int): Int {
        for (i in 0..y - blockSize) {
            var emptySpace = 0
            for (j in 0 until blockSize) {
                if (newParts[i + j].empty) {
                    emptySpace++
                } else {
                    break
                }
            }
            if (emptySpace == blockSize) {
                return i
            }
        }
        return -1
    }

}

fun main() {
    val day = 9
    println("Day $day part1 of Advent of Code 2024")
    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(1928)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")//too low 826040010 , too low 826040011
}

private fun part2(day: Int) {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(2858)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Long {
    return fileToInputD9(fileName).getScore()
}

private fun getScorePart2(fileName: String): Long {
    return fileToInputD9(fileName).getScoreV2()
}

fun fileToInputD9(fileName: String): InputD9 {
    val lines = File(fileName).readLines()
    val chars = lines.first().toList()
    val parts = mutableListOf<Part>()
    for (i in 0 until chars.size / 2) {
        val first = chars[i * 2].toString().toInt()
        val second = chars[i * 2 + 1].toString().toInt()

        for (j in 0 until first) {
            parts.add(Part(false, i.toLong()))
        }

        for (j in 0 until second) {
            parts.add(Part(true, 0))
        }
    }
    if (chars.size % 2 == 1) {
        val last = chars.last().toString().toInt()
        for (j in 0 until last) {
            parts.add(Part(false, chars.size / 2L))
        }
    }

    return InputD9(parts)
}