package game

import java.util.*

class StrategoUtil {
    companion object {
        private val directions = arrayOf(intArrayOf(-1, -1), intArrayOf(1, 1), intArrayOf(-1, 1), intArrayOf(1, -1))

        fun calculatePoints(move: Pair<Int, Int>, board: Array<IntArray>): Int {
            var points = 0

            if (checkRow(move.first, board))
                points += board.size
            if (checkColumn(move.second, board))
                points += board.size

            var pointsDiagonal = checkDiagonal(move.first, move.second, 0, board)
            var pointsDiagonalOffset = checkDiagonal(move.first, move.second, 2, board)
            if (pointsDiagonal > 0)
                pointsDiagonal++
            if (pointsDiagonalOffset > 0)
                pointsDiagonalOffset++

            points += pointsDiagonal + pointsDiagonalOffset
            return points
        }

        private fun checkDiagonal(x: Int, y: Int, offset: Int, board: Array<IntArray>): Int {
            var points = 0
            for (i in offset until directions.size / 2 + offset) {
                var tmpX = x + directions[i][0]
                var tmpY = y + directions[i][1]

                while (tmpX >= 0 && tmpX < board.size && tmpY >= 0 && tmpY < board.size) {
                    if (board[tmpX][tmpY] == 0)
                        return 0

                    tmpX += directions[i][0]
                    tmpY += directions[i][1]
                    points++
                }
            }
            return points
        }

        private fun checkRow(x: Int, board: Array<IntArray>): Boolean {
            for (i in board.indices) {
                if (board[x][i] == 0)
                    return false
            }
            return true
        }

        private fun checkColumn(y: Int, board: Array<IntArray>): Boolean {
            for (i in board.indices) {
                if (board[i][y] == 0)
                    return false
            }
            return true
        }

        private fun getRandomMove(list: List<Pair<Int, Int>>): Pair<Int, Int> {
            val rnd = Random()
            return list[rnd.nextInt(list.size)]
        }

        private fun getPossibleMoves(board: Array<IntArray>): List<Pair<Int, Int>> {
            val moveList = mutableListOf<Pair<Int, Int>>()
            for (x in board.indices) {
                for (y in board.indices) {
                    if (board[x][y] == 0) {
                        moveList.add(Pair(x, y))
                    }
                }
            }
            return moveList
        }
    }
}