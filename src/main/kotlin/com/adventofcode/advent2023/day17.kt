package com.adventofcode.advent2023

import java.io.File
import java.util.PriorityQueue

data class Node(val x: Int, val y: Int, val g: Int, val h: Int, val parent: Node?) : Comparable<Node> {
    val f: Int
        get() = g + h

    override fun compareTo(other: Node): Int {
        return this.f.compareTo(other.f)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Node) return false
        return this.x == other.x && this.y == other.y
    }
}


class DefaultXStar(val grid: Array<IntArray>) {
    fun heuristic(a: Node, b: Node): Int {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
    }

    fun aStar(start: Node, goal: Node): List<Node> {
        val openSet = PriorityQueue<Node>()
        val closedSet = mutableSetOf<Node>()
        openSet.add(start)

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()

            if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(current)
            }

            closedSet.add(current)

            for (neighbor in getNeighbors(current)) {
                if (closedSet.contains(neighbor)) continue

                val tentativeG = current.g + grid[neighbor.y][neighbor.x]

                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    val h = heuristic(neighbor, goal)
                    val newNode = Node(neighbor.x, neighbor.y, tentativeG, h, current)
                    openSet.add(newNode)
                }
            }
        }

        return emptyList()
    }

    fun getNeighbors(node: Node): List<Node> {
        val neighbors = mutableListOf<Node>()
        val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

        for (direction in directions) {
            val newX = node.x + direction.first
            val newY = node.y + direction.second

            if (newX in grid.indices && newY in grid[0].indices) {
                neighbors.add(Node(newX, newY, node.g + grid[newY][newX], 0, node))
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
    val filePath = "src/main/resources/2023/sampleDay17-1.txt"
    val grid = loadFileInto2DArray(filePath)

    val aStar = DefaultXStar(grid)
    val start = Node(0, 0, 0, 0, null)
    val goal = Node(grid.size - 1, grid[0].size - 1, 0, 0, null)

    val path = aStar.aStar(start, goal)

    if (path.isNotEmpty()) {
        println("Path found:")
        for (node in path) {
            println("(${node.x}, ${node.y})")
        }
        path.map { grid[it.y][it.x] }.sum().let { println("Total cost: $it") }
    } else {
        println("No path found")
    }
}

fun loadFileInto2DArray(filePath: String): Array<IntArray> {
    return File(filePath).readLines().map { line ->
        line.map { it.toString().toInt() }.toIntArray()
    }.toTypedArray()
}