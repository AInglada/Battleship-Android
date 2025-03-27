package com.example.battleship

import kotlin.random.Random

class GameManager(
    private val boardSize: Int = 8,
    private val ships: List<Int> = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
) {
    fun generateBoard(): Array<Array<Boolean>> {
        val board = Array(boardSize) { Array(boardSize) { false } }
        ships.forEach { placeShip(board, it) }
        return board
    }

    private fun placeShip(board: Array<Array<Boolean>>, length: Int) {
        repeat(100) {
            val x = Random.nextInt(boardSize)
            val y = Random.nextInt(boardSize)
            val horizontal = Random.nextBoolean()
            if (canPlaceShip(board, x, y, length, horizontal)) {
                (0 until length).forEach { i ->
                    if (horizontal) board[x + i][y] = true else board[x][y + i] = true
                }
                return
            }
        }
    }

    private fun canPlaceShip(board: Array<Array<Boolean>>, x: Int, y: Int, length: Int, horizontal: Boolean): Boolean {
        if (horizontal && x + length > boardSize) return false
        if (!horizontal && y + length > boardSize) return false
        return (0 until length).all { i ->
            val xi = if (horizontal) x + i else x
            val yi = if (horizontal) y else y + i
            isSpaceFree(board, xi, yi)
        }
    }

    private fun isSpaceFree(board: Array<Array<Boolean>>, x: Int, y: Int): Boolean {
        for (dx in -1..1) {
            for (dy in -1..1) {
                val nx = x + dx
                val ny = y + dy
                if (nx in 0 until boardSize && ny in 0 until boardSize && board[nx][ny]) return false
            }
        }
        return true
    }

    fun initializeHits() = Array(boardSize) { Array(boardSize) { false } }

    fun isAllShipsSunk(board: Array<Array<Boolean>>, hits: Array<Array<Boolean>>) =
        board.indices.all { row -> board[row].indices.all { col -> !board[row][col] || hits[row][col] } }
}