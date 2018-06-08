package game

import MAX_DEPTH_LEVEL
import ui.board.Field
import ui.board.Status
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

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
                makeMove(move)
                if (isRedPlayerTurn) {
                    redPoints += StrategoUtil.calculatePoints(move, simpleBoard)
                } else {
                    bluePoints += StrategoUtil.calculatePoints(move, simpleBoard)
                }
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
                makeMove(nextMove!!)
                redPoints += StrategoUtil.calculatePoints(nextMove, simpleBoard)
            } else {
                when (playerBlueType) {
                    Player.MIN_MAX -> nextMove = minMax()
                    Player.ALFA_BETA -> nextMove = alphaBeta()
                }
                makeMove(nextMove!!)
                bluePoints += StrategoUtil.calculatePoints(nextMove, simpleBoard)
            }
            printStateOfGame()
            isRedPlayerTurn = !isRedPlayerTurn
            moveCount++
        }
    }

    fun updateBoard(board: Array<Array<Field>>) {
        this.board = board
        simpleBoard = Array(board.size) { IntArray(board.size) { 0 } }
        for (x in board.indices) {
            for (y in board.indices) {
                board[x][y].addActionListener(this)
            }
        }
        resumeGame()
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
        simpleBoard = Array(board.size) { IntArray(board.size) { 0 } }
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
        val node = maxMove(Node(simpleBoard), 0)
        return node.move
    }


    private fun maxMove(node: Node, depth: Int): Node {
        var bestMaxNode: Node? = null
        if (isLastPossibleMove(depth)) {
            return node.getChildren()[0]
        }
        if (depth == MAX_DEPTH_LEVEL) {
            return node
        } else {
            for (child in node.getChildren()) {
                val candidateNode = minMove(child, depth + 1)
                if (bestMaxNode == null)
                    bestMaxNode = child
                else if (bestMaxNode.getValue() < candidateNode.getValue()) {
                    bestMaxNode = child
                }
            }
        }
        return bestMaxNode!!
    }

    private fun minMove(node: Node, depth: Int): Node {
        var bestMinNode: Node? = null
        if (isLastPossibleMove(depth)) {
            return node.getChildren()[0]
        }
        if (depth == MAX_DEPTH_LEVEL) {
            return node
        } else {
            for (child in node.getChildren()) {
                val candidateNode = maxMove(child, depth + 1)
                if (bestMinNode == null)
                    bestMinNode = child
                else if (bestMinNode.getValue() > candidateNode.getValue())
                    bestMinNode = child
            }
        }
        return bestMinNode!!
    }

    fun alphaBeta(): Pair<Int, Int> {
        val node = alphaBeta(Node(simpleBoard), Int.MIN_VALUE, Int.MAX_VALUE, 0)
        return node.move
    }

    fun alphaBeta(node: Node, alpha: Int, beta: Int, depth: Int): Node {
        var bestMoveValue: Int
        var bestNode: Node? = null

        if (isLastPossibleMove(depth)) {
            return node.getChildren()[0]
        }
        if (depth == MAX_DEPTH_LEVEL) {
            return node
        } else {
            if (!node.wasMaxPlayerTurn) {
                bestMoveValue = alpha

                loop@ for (child in node.getChildren()) {
                    val candidate = alphaBeta(child, bestMoveValue, beta, depth + 1)
                    if (bestNode == null) {
                        bestNode = child
                        bestMoveValue = candidate.getValue()
                    } else if (bestMoveValue < candidate.getValue()) {
                        bestNode = child
                        bestMoveValue = candidate.getValue()
                    }
                    if (beta <= bestMoveValue) {
                        break@loop
                    }
                }
            } else {
                bestMoveValue = beta

                loop@ for (child in node.getChildren()) {
                    val candidate = alphaBeta(child, alpha, bestMoveValue, depth + 1)
                    if (bestNode == null) {
                        bestNode = child
                        bestMoveValue = candidate.getValue()
                    } else if (bestMoveValue > candidate.getValue()) {
                        bestNode = child
                        bestMoveValue = candidate.getValue()
                    }
                    if (alpha >= bestMoveValue) {
                        break@loop
                    }
                }
            }
        }
        return bestNode!!
    }

    private fun isLastPossibleMove(depth: Int): Boolean {
        return moveCount + depth == board.size * board.size - 1
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

    private fun printStateOfGame() {
        System.out.println("Move: $moveCount RedPoints: $redPoints Blue Points: $bluePoints")
    }
}