package com.adventofcode.advent2023

import org.assertj.core.api.Assertions.assertThat
import java.io.File
import java.util.PriorityQueue

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);
}

data class Coordinates(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Coordinates) return false
        return this.x == other.x && this.y == other.y
    }

    override fun hashCode(): Int {
        return x + (y * 100)
    }
}

data class Node(val coordinates: Coordinates, val g: Int, val h: Int, val parent: Node?, val direction : Direction, val straightCount : Int) : Comparable<Node> {
    val f: Int
        get() = g + h

    override fun compareTo(other: Node): Int {
        return this.f.compareTo(other.f)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Node) return false
        return this.coordinates == other.coordinates
                && this.direction == other.direction
    }

    override fun hashCode(): Int {
        return coordinates.x + (coordinates.y * 100) + (direction.x * 10000)
    }
}


class XStarLimitByDirectionCount(val grid: Array<IntArray>) {
    fun heuristic(a: Node, b: Node): Int {
        return Math.abs(a.coordinates.x - b.coordinates.x) + Math.abs(a.coordinates.y - b.coordinates.y)
    }

    fun aStar(start: Node, goal: Node): List<Node> {
        val openSet = PriorityQueue<Node>()
        val closedSet = mutableSetOf<Coordinates>()
        openSet.add(start)

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()

            if (current.coordinates == goal.coordinates) {
                return reconstructPath(current)
            }

            closedSet.add(current.coordinates)

            val neighbors = getNeighbors(current)
            for (neighbor in neighbors) {
                if (closedSet.contains(neighbor.coordinates)) continue

                val tentativeG = current.g + grid[neighbor.coordinates.y][neighbor.coordinates.x]

                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    val h = heuristic(neighbor, goal)
                    val newNode = Node(neighbor.coordinates, tentativeG, h, current, neighbor.direction, neighbor.straightCount)
                    openSet.add(newNode)
                }
            }
        }

        return emptyList()
    }

    fun getNeighbors(node: Node): List<Node> {
        val neighbors = mutableListOf<Node>()
        val directions = Direction.entries

        for (direction in directions) {
            val newX = node.coordinates.x + direction.x
            val newY = node.coordinates.y + direction.y
            val newCoordinates = Coordinates(newX, newY)


            if (newY in grid.indices && newX in grid[0].indices && (node.parent == null || newCoordinates != node.parent.coordinates)) {
                val directionCount = if (direction == node.direction) node.straightCount + 1 else 0
                if (directionCount > 2) continue
                neighbors.add(Node(newCoordinates, node.g + grid[newY][newX], 0, node, direction, directionCount))
            }
        }

        return neighbors
    }

    fun reconstructPath(node: Node): List<Node> {
        var current: Node? = node
        val path = mutableListOf<Node>()

        while (current != null) {
            path.add(current)
            current = current.parent
        }

        return path.reversed()
    }
}

fun main() {
    part1()
}

private fun part1() {
    assertThat(shortestPath("src/main/resources/2023/sampleDay17-1.txt")).isEqualTo(102)
    val score = shortestPath("src/main/resources/2023/inputDay17.txt")
    //906,907 is too low
    println("result for input is: $score")
}

private fun shortestPath(filePath: String): Int {
    val grid = loadFileInto2DArray(filePath)
    val aStar = XStarLimitByDirectionCount(grid)
    val startCoordinates = Coordinates(0, 0)
    val goalCoordinates = Coordinates(grid[0].size - 1, grid.size - 1)
    val start = Node(startCoordinates, 0, 0, null, Direction.RIGHT, 0)
    val goal = Node(goalCoordinates, 0, 0, null, Direction.UP, 0)

    val path = aStar.aStar(start, goal)
    val score = calculateScore(path, grid)
    printPath(path)
    return score
}

private fun printPath(path: List<Node>) {
    if (path.isNotEmpty()) {
        println("Path found:")
        for (node in path) {
            println("(${node.coordinates.x}, ${node.coordinates.y})")
        }
    } else {
        println("No path found")
    }
}

private fun calculateScore(
    path: List<Node>,
    grid: Array<IntArray>
): Int {
    val score = path.map { grid[it.coordinates.y][it.coordinates.x] }.sum()
    return score
}

fun loadFileInto2DArray(filePath: String): Array<IntArray> {
    return File(filePath).readLines().map { line ->
        line.map { it.toString().toInt() }.toIntArray()
    }.toTypedArray()
}