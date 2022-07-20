package me.farme.application

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HtmlController {
    @GetMapping("/")
    @ResponseBody
    fun index() = createHTML().html {
        head {
            title("ToDo List")
        }
        body {
            h1{+"Hello"}
            div {
                id = "root"
            }
            script(src = "/main.js") {}
        }
    }
}