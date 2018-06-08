package ui.board

import java.awt.Color
import javax.swing.JButton

enum class Status {
    RED,
    BLUE
}

class Field : JButton() {

    var isEmpty = true
    lateinit var status: Status

    init {
        background = Color.LIGHT_GRAY
        isOpaque = true
    }

    fun changeStatus(status: Status) {
        if (isEmpty) {
            this.status = status
            isEmpty = false
            background = when (status) {
                Status.RED -> Color.RED
                Status.BLUE -> Color.BLUE
            }
            isOpaque = true
        } else {
            throw IllegalStateException("Field is already taken")
        }
    }
}