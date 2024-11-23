package com.adventofcode.advent2022

import org.assertj.core.api.Assertions.assertThat
import java.io.File

fun main() {
    val day = "Day 3"
    println("$day part1 of Advent of Code 2022")
    part1()
    println("$day part2 of Advent of Code 2022")
    part2()
}

private fun part1() {
    val score = getScore("src/main/resources/2022/inputSampleDay3-1.txt")
    assertThat(score).isEqualTo(157)

    val score2 = getScore("src/main/resources/2022/inputDay3.txt")
    println("result for input is: $score2")
}

private fun part2() {
//    val score = getScoreP2("src/main/resources/2022/inputSampleDay2-1.txt")
//    assertThat(score).isEqualTo(12)
//
//    val score2 = getScoreP2("src/main/resources/2022/inputDay2.txt")
//    println("result for input is: $score2")
}



private fun getScore(fileName: String): Int {
    val ruckSacks = fileToRuckSacks(fileName);
    return ruckSacks.map { it.getIntersection().toNumber()}.sum();
}

fun Char.toNumber(): Int {
    return when (this) {
        in 'a'..'z' -> this - 'a' + 1
        in 'A'..'Z' -> this - 'A' + 27
        else -> throw IllegalArgumentException("Character must be between 'a' and 'z' or 'A' and 'Z'")
    }
}

//private fun getScoreP2(fileName: String): Int {
//    val hands = fileToHandsPart2(fileName);
//    return hands.map { it.score() }.sum();
//}

class RuckSack(val comp1: List<Char>, val comp2: List<Char>) {

    private fun getDuplicateChars(): List<Char> {
        return comp1.intersect(comp2).toList()
    }

    fun getIntersection(): Char {
        val duplicates = getDuplicateChars();
        if (duplicates.size != 1) {
            throw Exception("Invalid rucksack")
        }
        return duplicates.first();
    }

}

fun fileToRuckSacks(fileName: String): List<RuckSack> {
    val lines = File(fileName).readLines()
    return lines.map { line ->
        val part1 = line.substring(0, line.length / 2)
        val part2 = line.substring(line.length / 2)
        RuckSack(part1.toList(), part2.toList())
    }

}

//fun fileToHandsPart2(fileName: String): List<Round> {
//    val lines = File(fileName).readLines()
//
//    return lines.map { line ->
//        val hands = line.split(" ")
//        val opponent = Hand.fromString(hands[0])
//        val my = Hand.fromStringAndOpponentHand(hands[1], opponent)
//        Round(opponent, my)
//    }
//}