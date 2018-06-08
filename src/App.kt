import game.StrategoUtil
import ui.MainWindow
import ui.board.BoardPanel
import ui.settings.SettingsPanel
import java.awt.BorderLayout

class App {

    private val mainWindow = MainWindow(APP_NAME)
    private val boardPanel = BoardPanel()
    private val settingsPanel = SettingsPanel(boardPanel)

    fun init() {
        mainWindow.add(settingsPanel, BorderLayout.LINE_END)
        mainWindow.add(boardPanel, BorderLayout.CENTER)
        mainWindow.pack()
    }
}