import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    val windowState = WindowState(
        size = DpSize(480.dp, 520.dp),
        position = WindowPosition(200.dp, 200.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Local Time App",
        state = windowState
    ) {
        App()
    }
}