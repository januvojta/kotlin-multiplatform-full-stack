import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import react.dom.render

fun main() {
    val welcome = Welcome.create {
        name = "Kotlin/JS"
    }

    val root = createRoot(document.getElementById("root")!!)
    root.render(welcome)
}