package reddit_bot.infrastructure.endpoint

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import reddit_bot.infrastructure.repository.SubredditPersistence
import reddit_bot.service.{LinkSender, LinkUpdater}

import scala.jdk.CollectionConverters.IterableHasAsJava

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

    @RequestMapping(Array("/listSubreddits"))
    @ResponseBody
    def listSubreddits: java.lang.Iterable[String] = {
        SubredditPersistence.findEnabled().unsafeRunSync()
            .map(_.toString).asJava
    }
}