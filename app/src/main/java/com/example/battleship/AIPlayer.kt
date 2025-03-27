package com.example.battleship

class AIPlayer(private val boardSize: Int = 8) {
    fun generateMove(hits: Array<Array<Boolean>>): Pair<Int, Int> {
        val availableCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (!hits[i][j]) availableCells.add(i to j)
            }
        }
        return availableCells.random()
    }
}