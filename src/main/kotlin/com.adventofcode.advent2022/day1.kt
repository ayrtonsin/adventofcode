package com.adventofcode.advent2022

import org.assertj.core.api.Assertions.assertThat
import java.io.File

fun main() {
    val day = "Day 1"
    println("$day part1 of Advent of Code 2022")
    part1()
    println("$day part2 of Advent of Code 2022")
    part2()
}

private fun part1() {
    val maxCarried = getMaxCarried("src/main/resources/2022/inputSampleDay1-1.txt")
    assertThat(maxCarried).isEqualTo(24000)

    val maxCarried2 = getMaxCarried("src/main/resources/2022/inputDay1.txt")
    println("result for input is: $maxCarried2")
}

private fun part2() {
    val maxCarried = getMaxCarriedForTop3("src/main/resources/2022/inputSampleDay1-1.txt")
    assertThat(maxCarried).isEqualTo(45000)

    val maxCarried2 = getMaxCarriedForTop3("src/main/resources/2022/inputDay1.txt")
    println("result for input is: $maxCarried2")
}

fun getMaxCarriedForTop3(fileName: String): Int {
    val elves = fileToElves(fileName)
    val top3 = elves.sortedByDescending { it.totalCaloriesCarried() }.take(3)
    return top3.sumOf { it.totalCaloriesCarried() }
}

private fun getMaxCarried(fileName: String): Int {
    val elves = fileToElves(fileName)
    return elves.maxOfOrNull { it.totalCaloriesCarried() } ?: 0
}

class Food(val calories: Int){
    override fun toString(): String {
        return "(calories=$calories)"
    }
}

class Elf(val carriedFood: List<Food>){
    fun totalCaloriesCarried(): Int {
        return carriedFood.sumOf { it.calories }
    }
}


fun fileToElves(fileName: String): List<Elf> {
    val lines = File(fileName).readLines()
    var foods = mutableListOf<Food>()
    var elves = mutableListOf<Elf>()
    for(line in lines) {
        if(line.isEmpty()) {
            val elf = Elf(foods.toList())
            elves.add(elf)
            foods.clear()
        }else {
            val food = Food(line.toInt())
            foods.add(food)
        }
    }
    if(foods.isNotEmpty()) {
        val elf = Elf(foods.toList())
        elves.add(elf)
    }
    return elves
}