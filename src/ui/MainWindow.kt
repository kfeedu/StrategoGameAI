package ui

import WINDOW_HEIGHT
import WINDOW_WIDTH
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants

class MainWindow(appTitle: String) : JFrame() {

    var verticalGap = 0
    var horizontalGap = 0

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        title = appTitle
        layout = BorderLayout(verticalGap, horizontalGap)
        contentPane.preferredSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        isResizable = false
        isVisible = true
    }
}