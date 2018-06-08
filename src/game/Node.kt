package game

class Node(
        val board: Array<IntArray>,
        val wasMaxPlayerTurn: Boolean,
        var maxPoints: Int,
        var minPoints: Int,
        var move: Pair<Int, Int>) {

    var bestValue: Int

    constructor(board: Array<IntArray>) : this(board, false, 0, 0, Pair(-1, -1))

    init {
        //storing last move and adding points
        if(move.first >= 0){
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

    fun getMaxChildNode(): Node {
        var bestMove: Pair<Int, Int>? = null
        var bestPoints = -1
        for (move in possibleMoves) {
            val movePoints = StrategoUtil.calculatePoints(move, board)
            if (movePoints > bestPoints) {
                bestMove = move
                bestPoints = movePoints
            }
        }
        return Node(board.deepCopy(), !wasMaxPlayerTurn, maxPoints, minPoints, bestMove!!)
    }

    private fun Array<IntArray>.deepCopy(): Array<IntArray> {
        val result = Array(size) { IntArray(size) { 0 } }
        for (i in indices) {
            result[i] = this[i].clone()
        }
        return result
    }
}

