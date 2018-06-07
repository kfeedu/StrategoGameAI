package game

import MAX_DEPTH_LEVEL
import ui.board.Field
import ui.board.Status
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*

enum class Player {
    MIN_MAX, ALFA_BETA, HUMAN
}

class Stratego : ActionListener {

    lateinit var board: Array<Array<Field>>
    lateinit var simpleBoard: Array<IntArray>

    var playerRedType = Player.HUMAN
    var playerBlueType = Player.HUMAN
    val playerRedValue = 1
    val playerBlueValue = -1
    private var isRedPlayerTurn = true

    var moveCount = 0
    var redPoints = 0
    var bluePoints = 0

    override fun actionPerformed(e: ActionEvent) {
        val field = e.source as Field
        if (field.isEmpty && isHumanTurn()) {
            val move = getXYOfField(field)
            if (move != null) {
                if (isRedPlayerTurn) {
                    redPoints += StrategoUtil.calculatePoints(move, simpleBoard)
                } else {
                    bluePoints += StrategoUtil.calculatePoints(move, simpleBoard)
                }
                makeMove(move)
            }
            isRedPlayerTurn = !isRedPlayerTurn
            moveCount++
            printStateOfGame()
            resumeGame()
        }
    }

    fun resumeGame() {
        while (moveCount < board.size * board.size && !isHumanTurn()) {
            var nextMove: Pair<Int, Int>? = null
            if (isRedPlayerTurn) {
                when (playerRedType) {
                    Player.MIN_MAX -> nextMove = minMax()
                    Player.ALFA_BETA -> nextMove = alphaBeta()
                }
                redPoints += StrategoUtil.calculatePoints(nextMove!!, simpleBoard)
            } else {
                when (playerBlueType) {
                    Player.MIN_MAX -> nextMove = minMax()
                    Player.ALFA_BETA -> nextMove = alphaBeta()
                }
                bluePoints += StrategoUtil.calculatePoints(nextMove!!, simpleBoard)
            }
            makeMove(nextMove)
            System.out.println("RedPoints: " + redPoints + " Blue Points: " + bluePoints)
            isRedPlayerTurn = !isRedPlayerTurn
            moveCount++
        }
    }

    fun updateBoard(board: Array<Array<Field>>) {
        this.board = board
        simpleBoard = Array(board.size) { IntArray(board.size) {0} }
        for (x in board.indices) {
            for (y in board.indices) {
                board[x][y].addActionListener(this)
            }
        }
    }

    fun updatePlayers(player1: Player, player2: Player) {
        this.playerRedType = player1
        this.playerBlueType = player2
        resetGame()
    }

    fun resetGame() {
        moveCount = 0
        isRedPlayerTurn = true
        redPoints = 0
        bluePoints = 0
        simpleBoard = Array(board.size) { IntArray(board.size) {0} }
        for (x in board.indices) {
            for (y in board.indices) {
                board[x][y].isEmpty = true
                board[x][y].background = Color.lightGray
            }
        }
        resumeGame()
    }

    private fun getCurrentFieldStatus(): Status {
        return if (isRedPlayerTurn) Status.RED else Status.BLUE
    }

    private fun isHumanTurn(): Boolean {
        return if (isRedPlayerTurn)
            playerRedType == Player.HUMAN
        else
            playerBlueType == Player.HUMAN
    }

    private fun minMax(): Pair<Int, Int> {
        return maxMove(deepCopy(simpleBoard), 0)
    }

    private fun maxMove(board: Array<IntArray>, depth: Int): Pair<Int, Int> {
        var listOfPossibleMoves = getPossibleMoves(board)
        var bestMove = getRandomMove(listOfPossibleMoves)
        if (isLastPossibleMove(board) || depth == MAX_DEPTH_LEVEL) {
            for (move in listOfPossibleMoves) {
                if (value(bestMove, board) < value(move, board)) {
                    bestMove = move
                }
            }
            return bestMove
        } else {
            for (move in listOfPossibleMoves) {
                val tempMove = minMove(makeMove(deepCopy(board), move), depth + 1)
                if (value(tempMove, board) > value(bestMove, board)) {
                    bestMove = tempMove
                }
            }
            return bestMove
        }
    }

    private fun minMove(board: Array<Array<Field>>, depth: Int): Pair<Int, Int> {
        var listOfPossibleMoves = getPossibleMoves(board)
        var bestMove = getRandomMove(listOfPossibleMoves)
        if (isLastPossibleMove(board) || depth == MAX_DEPTH_LEVEL) {
            for (move in listOfPossibleMoves) {
                if (value(bestMove, board) > value(move, board)) {
                    bestMove = move
                }
            }
            return bestMove
        } else {
            for (move in listOfPossibleMoves) {
                val tempMove = maxMove(makeMove(deepCopy(board), move), depth + 1)
                if (value(tempMove, board) < value(bestMove, board)) {
                    bestMove = tempMove
                }
            }
            return bestMove
        }
    }

    private fun deepCopy(array: Array<Array<Field>>): Array<Array<Field>> {
        val result = Array(array.size) { Array(array.size) { Field() } }
        for (i in array.indices) {
            var tempInside = Array(array.size) { Field() }
            for (j in array.indices) {
                tempInside[j].isEmpty = array[i][j].isEmpty
            }
            result[i] = tempInside
        }
        return result
    }

    private fun deepCopy(array: Array<IntArray>): Array<IntArray>{
        val result = Array(array.size) { IntArray(array.size) {0} }
        for(i in array.indices){
            result[i] = array[i].clone()
        }
        return result
    }

    private fun isLastPossibleMove(board: Array<Array<Field>>): Boolean {
        var moveCount = 0
        for (x in board.indices) {
            for (y in board.indices) {
                if (board[x][y].isEmpty) moveCount++
            }
        }
        return moveCount <= 1
    }



    private fun makeMove(move: Pair<Int, Int>) {
        board[move.first][move.second].changeStatus(getCurrentFieldStatus())
        simpleBoard[move.first][move.second] = isRedPlayerTurn.toPlayerValue()
    }

    fun Boolean.toPlayerValue() = if (this) playerRedValue else playerBlueValue

    private fun getXYOfField(field: Field): Pair<Int, Int>? {
        for (x in board.indices) {
            for (y in board.indices) {
                if (board[x][y] == field) {
                    return Pair(x, y)
                }
            }
        }
        return null
    }

    //ALFA BETA

    fun alphaBeta(): Pair<Int, Int> {
        return alphaBeta(board, Int.MIN_VALUE, Int.MAX_VALUE, true, 0)!!
    }

    fun alphaBeta(board: Array<Array<Field>>, alpha: Int, beta: Int, maximisingPlayer: Boolean, depth: Int): Pair<Int, Int>? {
        val possibleMoves = getPossibleMoves(board)
        if (possibleMoves.size == 1) {
            return possibleMoves[0]
        }
        var bestMove: Pair<Int, Int>? = null
        var bestMoveValue: Int

        if (maximisingPlayer) {
            bestMoveValue = alpha

            if (depth == MAX_DEPTH_LEVEL) {
                var lastDepthMove = getRandomMove(possibleMoves)
                var lastDepthMoveValue = value(lastDepthMove, board)
                for (move in possibleMoves) {
                    val childBestValue = value(move, board)
                    if (lastDepthMoveValue < childBestValue) {
                        lastDepthMove = move
                        lastDepthMoveValue = childBestValue
                    }
                }
                return lastDepthMove
            } else {
                loop@ for (move in possibleMoves) {
                    val childBestMove = alphaBeta(makeMove(deepCopy(board), move), bestMoveValue, beta, false, depth + 1)
                    if (childBestMove != null) {
                        val childMoveValue = value(childBestMove, board)
                        if (bestMoveValue <= childMoveValue) {
                            bestMove = childBestMove
                            bestMoveValue = childMoveValue
                        }
                        if (beta <= bestMoveValue) {
                            break@loop
                        }
                    }
                }
            }
        } else {
            bestMoveValue = beta

            if (depth == MAX_DEPTH_LEVEL) {
                var lastDepthMove = getRandomMove(possibleMoves)
                var lastDepthMoveValue = value(lastDepthMove, board)
                for (move in possibleMoves) {
                    val childBestValue = value(move, board)
                    if (lastDepthMoveValue > childBestValue) {
                        lastDepthMove = move
                        lastDepthMoveValue = childBestValue
                    }
                }
                return lastDepthMove
            } else {
                loop@ for (move in possibleMoves) {
                    val childBestMove = alphaBeta(makeMove(deepCopy(board), move), alpha, bestMoveValue, true, depth + 1)
                    if (childBestMove != null) {
                        val childMoveValue = value(childBestMove, board)
                        if (bestMoveValue >= childMoveValue) {
                            bestMove = childBestMove
                            bestMoveValue = childMoveValue
                        }
                        if (bestMoveValue <= alpha) {
                            break@loop
                        }
                    }
                }
            }
        }
        return bestMove
    }

    private fun printStateOfGame() {
        System.out.println("Move: $moveCount RedPoints: $redPoints Blue Points: $bluePoints")
    }
}