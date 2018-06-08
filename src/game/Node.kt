package game

class Node(
        val board: Array<IntArray>,
        val wasMaxPlayerTurn: Boolean,
        var maxPoints: Int,
        var minPoints: Int,
        var move: Pair<Int, Int>) {

    constructor(board: Array<IntArray>) : this(board, false, 0, 0, Pair(-1, -1))

    companion object {
        var countNodeCreation = 0
    }

    var bestValue: Int

    init {
        countNodeCreation++
        //storing last move and adding points
        if (move.first >= 0) {
            if (wasMaxPlayerTurn) {
                board[move.first][move.second] = 1
                maxPoints += StrategoUtil.calculatePoints(move, board)
            } else {
                board[move.first][move.second] = -1
                minPoints += StrategoUtil.calculatePoints(move, board)
            }
        }
        bestValue = getValue()
    }

    val possibleMoves: List<Pair<Int, Int>> = StrategoUtil.getPossibleMoves(board)

    fun getValue(): Int = maxPoints - minPoints

    fun getChildren(): List<Node> {
        val children = mutableListOf<Node>()
        for (move in possibleMoves) {
            children.add(Node(board.deepCopy(), !wasMaxPlayerTurn, maxPoints, minPoints, move))
        }
        return children
    }

    private fun Array<IntArray>.deepCopy(): Array<IntArray> {
        val result = Array(size) { IntArray(size) { 0 } }
        for (i in indices) {
            result[i] = this[i].clone()
        }
        return result
    }
}

