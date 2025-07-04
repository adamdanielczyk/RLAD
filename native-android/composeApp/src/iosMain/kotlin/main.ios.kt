import androidx.compose.ui.window.ComposeUIViewController
import com.rlad.composeapp.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }