package ui.board

import BOARD_HEIGHT
import BOARD_WIDTH
import STARTING_BOARD_SIZE
import game.Player
import game.Stratego
import ui.settings.SettingsPanel
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class BoardPanel : JPanel(), SettingsPanel.SettingsListener {

    private var boardSize = STARTING_BOARD_SIZE
    lateinit var fieldsArray: Array<Array<Field>>

    val gameManager = Stratego()

    init {
        preferredSize = Dimension(BOARD_WIDTH, BOARD_HEIGHT)
        layout = GridBagLayout()
        updateGUI()
    }

    private fun updateGUI() {
        removeAll()
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        fieldsArray = Array(boardSize) { Array(boardSize) { Field() } }

        if (fieldsArray.isNotEmpty()) {
            val fieldSize = calculateFieldSize()
            for (x in fieldsArray.indices) {
                for (y in fieldsArray.indices) {
                    constraints.gridx = x
                    constraints.gridy = y
                    fieldsArray[x][y] = Field()
                    fieldsArray[x][y].preferredSize = Dimension(fieldSize, fieldSize)
                    add(fieldsArray[x][y], constraints)
                }
            }
        }
        revalidate()
    }

    private fun calculateFieldSize(): Int = minOf(BOARD_WIDTH, BOARD_HEIGHT) / boardSize

    override fun updateSize(n: Int) {
        boardSize = n
        updateGUI()
        gameManager.updateBoard(fieldsArray)
    }

    override fun changeStrategy(player1: Player, player2: Player) {
        gameManager.updatePlayers(player1, player2)
    }
}