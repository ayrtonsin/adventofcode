package com.adventofcode.advent2022

import org.assertj.core.api.Assertions.assertThat
import java.io.File

fun main() {
    val day = "Day 2"
    println("$day part1 of Advent of Code 2022")
    part1()
    println("$day part2 of Advent of Code 2022")
    part2()
}

private fun part1() {
    val score = getScore("src/main/resources/2022/inputSampleDay2-1.txt")
    assertThat(score).isEqualTo(15)

    val score2 = getScore("src/main/resources/2022/inputDay2.txt")
    println("result for input is: $score2")
}

private fun part2() {
    val score = getScoreP2("src/main/resources/2022/inputSampleDay2-1.txt")
    assertThat(score).isEqualTo(12)

    val score2 = getScoreP2("src/main/resources/2022/inputDay2.txt")
    println("result for input is: $score2")
}



private fun getScore(fileName: String): Int {
    val hands = fileToHands(fileName);
    return hands.map { it.score() }.sum();
}

private fun getScoreP2(fileName: String): Int {
    val hands = fileToHandsPart2(fileName);
    return hands.map { it.score() }.sum();
}

enum class Hand(val score: Int){
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        fun fromString(s: String): Hand {
            return when (s) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw Exception("Invalid hand")
            }
        }

        fun fromStringAndOpponentHand(s: String, opponent: Hand): Hand {
            return when (s) {
                "X" -> {//loose
                    when (opponent) {
                        ROCK -> SCISSORS
                        PAPER -> ROCK
                        SCISSORS -> PAPER
                    }
                }
                //draw
                "Y" -> opponent
                "Z" -> {//win
                    when (opponent) {
                        ROCK -> PAPER
                        PAPER -> SCISSORS
                        SCISSORS -> ROCK
                    }
                }
                else -> throw Exception("Invalid hand")
            }
        }
    }
}

class Round(val opponent: Hand, val my: Hand){
    fun score(): Int {
        if (opponent == my)
            return 3 + my.score
        else if(opponent == Hand.ROCK)
            if(my == Hand.PAPER)
                return 6 + my.score
            else
                return 0 + my.score
        else if(opponent == Hand.PAPER)
            if(my == Hand.SCISSORS)
                return 6 + my.score
            else
                return 0 + my.score
        else if(opponent == Hand.SCISSORS)
            if(my == Hand.ROCK)
                return 6 + my.score
            else
                return 0 + my.score
        else
            throw Exception("Invalid round")
    }
}


fun fileToHands(fileName: String): List<Round> {
    val lines = File(fileName).readLines()

    return lines.map { line ->
        val hands = line.split(" ")
        val oponent = Hand.fromString(hands[0])
        val my = Hand.fromString(hands[1])
        Round(oponent, my)
    }
}

fun fileToHandsPart2(fileName: String): List<Round> {
    val lines = File(fileName).readLines()

    return lines.map { line ->
        val hands = line.split(" ")
        val opponent = Hand.fromString(hands[0])
        val my = Hand.fromStringAndOpponentHand(hands[1], opponent)
        Round(opponent, my)
    }
}