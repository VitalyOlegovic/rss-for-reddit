package reddit_bot
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class Prova{

    @RequestMapping(Array("/scala"))
    @ResponseBody
    def ciao() : String = {
        "Ciao mondo!"
    }
}