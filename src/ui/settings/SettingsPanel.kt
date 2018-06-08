package ui.settings

import SETTINGS_HEIGHT
import SETTINGS_WIDTH
import game.Player
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*


class SettingsPanel(private val listener: SettingsListener) : JPanel(), ActionListener {

    private val sizeBtn = JButton("SET")
    private val sizeTextField = JTextField()

    private val humanVsHumanBtn = JButton("Human vs Human")
    private val humanVsMinMaxBtn = JButton("Human vs MinMax")
    private val humanVsAlfaBetaBtn = JButton("Human vs AlfaBeta")
    private val minMaxVsMinMaxBtn = JButton("MinMax vs MinMax")
    private val minMaxVsAlfaBetaBtn = JButton("MinMax vs AlfaBeta")
    private val alfaBetaVsAlfaBetaBtn = JButton("AlfaBeta vs AlfaBeta")

    init {
        preferredSize = Dimension(SETTINGS_WIDTH, SETTINGS_HEIGHT)
        layout = GridBagLayout()
        createGUI()
    }

    interface SettingsListener {
        fun updateSize(n: Int)

        fun changeStrategy(player1: Player, player2: Player)
    }

    override fun actionPerformed(e: ActionEvent) {
        if (e.source is JButton) {
            resetButtonsColor()
            (e.source as JButton).background = Color.GREEN
        }
        when (e.source) {
            sizeBtn -> {
                if (sizeTextField.text.isNotEmpty() && sizeTextField.text.toInt() > 0) {
                    listener.updateSize(sizeTextField.text.toInt())
                }
            }
            humanVsHumanBtn -> {
                listener.changeStrategy(Player.HUMAN, Player.HUMAN)
            }
            humanVsAlfaBetaBtn -> {
                listener.changeStrategy(Player.HUMAN, Player.ALFA_BETA)
            }
            humanVsMinMaxBtn -> {
                listener.changeStrategy(Player.HUMAN, Player.MIN_MAX)
            }
            minMaxVsMinMaxBtn -> {
                listener.changeStrategy(Player.MIN_MAX, Player.MIN_MAX)
            }
            minMaxVsAlfaBetaBtn -> {
                listener.changeStrategy(Player.MIN_MAX, Player.ALFA_BETA)
            }
            alfaBetaVsAlfaBetaBtn -> {
                listener.changeStrategy(Player.ALFA_BETA, Player.ALFA_BETA)
            }
        }
    }

    private fun resetButtonsColor() {
        humanVsHumanBtn.background = Color.LIGHT_GRAY
        humanVsMinMaxBtn.background = Color.LIGHT_GRAY
        humanVsAlfaBetaBtn.background = Color.LIGHT_GRAY
        minMaxVsAlfaBetaBtn.background = Color.LIGHT_GRAY
        minMaxVsMinMaxBtn.background = Color.LIGHT_GRAY
        alfaBetaVsAlfaBetaBtn.background = Color.LIGHT_GRAY
    }

    private fun createGUI() {
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL

        //setting button listeners
        sizeBtn.addActionListener(this)
        humanVsHumanBtn.addActionListener(this)
        humanVsMinMaxBtn.addActionListener(this)
        humanVsAlfaBetaBtn.addActionListener(this)
        minMaxVsAlfaBetaBtn.addActionListener(this)
        minMaxVsMinMaxBtn.addActionListener(this)
        alfaBetaVsAlfaBetaBtn.addActionListener(this)

        resetButtonsColor()

        //SIZE AREA
        val lineLabel = JLabel("BOARD SIZE")
        constraints.gridx = 1
        constraints.gridy = 0
        constraints.gridwidth = 2
        constraints.insets = Insets(0, 0, 10, 0)
        add(lineLabel, constraints)

        constraints.gridx = 0
        constraints.gridy = 1

        add(sizeBtn, constraints)

        constraints.gridx = 2
        constraints.gridy = 1
        constraints.gridwidth = 2
        add(sizeTextField, constraints)

        constraints.insets = Insets(20, 0, 0, 0)
        constraints.gridx = 0
        constraints.gridy = 3
        constraints.gridwidth = 3
        add(humanVsHumanBtn, constraints)

        constraints.gridx = 0
        constraints.gridy = 4
        constraints.gridwidth = 3
        add(humanVsMinMaxBtn, constraints)

        constraints.gridx = 0
        constraints.gridy = 5
        constraints.gridwidth = 3
        add(humanVsAlfaBetaBtn, constraints)

        constraints.gridx = 0
        constraints.gridy = 6
        constraints.gridwidth = 3
        add(minMaxVsAlfaBetaBtn, constraints)

        constraints.gridx = 0
        constraints.gridy = 7
        constraints.gridwidth = 3
        add(minMaxVsMinMaxBtn, constraints)

        constraints.gridx = 0
        constraints.gridy = 8
        constraints.gridwidth = 3
        add(alfaBetaVsAlfaBetaBtn, constraints)
    }
}