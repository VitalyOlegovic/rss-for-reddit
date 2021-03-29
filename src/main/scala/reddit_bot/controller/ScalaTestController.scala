package reddit_bot.Controller
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import reddit_bot.service.LinkUpdater

@Controller
class Prova(
    @Autowired linkUpdater: LinkUpdater
){

    @RequestMapping(Array("/updateLinks"))
    @ResponseBody
    def updateLinks() : String = {
        linkUpdater.updateFeeds
        "Feeds updated"
    }
}