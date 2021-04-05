package reddit_bot.Controller
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import reddit_bot.service.LinkUpdater
import reddit_bot.service.LinkSender

@Controller
class MainController(
    @Autowired linkUpdater: LinkUpdater,
    @Autowired linkSender: LinkSender
){

    @RequestMapping(Array("/updateLinks"))
    @ResponseBody
    def updateLinks() : String = {
        linkUpdater.updateFeeds
        "Feeds updated"
    }

    @RequestMapping(Array("/linksSend"))
    @ResponseBody
    def linksSend() : String = {
        linkSender.send
        "Links sent"
    }
}