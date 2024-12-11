package com.adventofcode.advent2024

import org.assertj.core.api.Assertions.assertThat
import java.io.File


data class Rule(val first: Int, val second: Int) {

}

data class Page(val pageNr: List<Int>) {

    fun getMiddlePage(): Int {
        return pageNr[pageNr.size / 2]
    }

    fun isValidPage(rulesAsMapInv: Map<Int, List<Int>>): Boolean {
        val forbiddenRules = mutableSetOf<Int>()
        for (nr in pageNr) {
            if (forbiddenRules.contains(nr)) {
                return false
            }
            if (rulesAsMapInv.containsKey(nr)) {
                forbiddenRules.addAll(rulesAsMapInv[nr]!!)
            }
        }
        return true
    }

    fun fixOrder(rules: List<Rule>): Page {
        val newOrder = pageNr.toMutableList()
        var changed: Boolean
        do {
            changed = false
            for (i in 0 until newOrder.size - 1) {
                val current = newOrder[i]
                val next = newOrder[i + 1]
                if (rules.contains(Rule(next, current))) {
                    newOrder[i] = next
                    newOrder[i + 1] = current
                    changed = true
                }
            }
        } while (changed)
        return Page(newOrder)
    }

}

data class Input(val rules: List<Rule>, val pages: List<Page>) {
    fun getValidPages(): List<Page> {
        val rulesAsMapInv = rules.groupBy { it.second }.mapValues { it.value.map(Rule::first) }
        return pages.filter { it.isValidPage(rulesAsMapInv) }
    }

    fun fixOrderByRulesOfInvalidPages(): List<Page> {
        return getInvalidPages()
            .map { it.fixOrder(rules) }
    }

    private fun getInvalidPages(): List<Page> {
        val rulesAsMapInv = rules.groupBy { it.second }.mapValues { it.value.map(Rule::first) }
        return pages.filter { !it.isValidPage(rulesAsMapInv) }
    }
}

fun main() {
    val day = 5
    println("Day $day part1 of Advent of Code 2024")
    part1(day)
    println("Day $day part2 of Advent of Code 2024")
    part2(day)// too low 5297
}

private fun part1(day: Int) {
    val score = getScorePart1("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(143)

    val score2 = getScorePart1("src/main/resources/2024/InputDay$day.txt")
    println("result for input is: $score2")
}

private fun part2(day: Int) {
    val score = getScorePart2("src/main/resources/2024/SampleDay$day.txt")
    assertThat(score).isEqualTo(123)

    val score2 = getScorePart2("src/main/resources/2024/InputDay$day.txt")
    println("result for input part2 is: $score2")
}

private fun getScorePart1(fileName: String): Int {
    return fileToInput(fileName).getValidPages()
        .map(Page::getMiddlePage)
        .sum()
}

private fun getScorePart2(fileName: String): Int {
    return fileToInput(fileName).fixOrderByRulesOfInvalidPages()
        .map(Page::getMiddlePage)
        .sum()
}

fun fileToInput(fileName: String): Input {
    val lines = File(fileName).readLines()
    //part before the empty line are rules
    val rules = mutableListOf<Rule>()
    val pages = mutableListOf<Page>()
    var rulesDone = false
    for (line in lines) {
        if (line.isEmpty()) {
            rulesDone = true
            continue
        }
        if (!rulesDone) {
            val parts = line.split("|").map { it.toInt() }
            rules.add(Rule(parts[0], parts[1]))
        } else {
            val nrs = line.split(",").map { it.toInt() }
            pages.add(Page(nrs))
        }
    }
    return Input(rules.toList(), pages.toList())
}