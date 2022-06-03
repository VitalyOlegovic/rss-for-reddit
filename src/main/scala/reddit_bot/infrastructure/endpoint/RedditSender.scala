package reddit_bot.infrastructure.endpoint

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.domain.entity.{Link, Subreddit}
import reddit_bot.service.LinkSender

import java.net.URL
import java.util.Date
import scala.jdk.CollectionConverters._
import reddit_bot.infrastructure.repository.SubredditPersistence
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.LinkSendingPersistence
import reddit_bot.infrastructure.repository.LinkSending
import java.time.LocalDateTime

@Service
class RedditSender(
    @Autowired redditSubmitter: RedditSubmitter,
    @Autowired linkSender: LinkSender
){
    private val logger = LoggerFactory.getLogger(classOf[RedditSender])
    private val subredditPersistence = new SubredditPersistence(Database.transactor)
    private val linkSendingPersistence = new LinkSendingPersistence(Database.transactor)

    def send: Unit = {
        for{
            enabled <- subredditPersistence.findEnabled()
            r = enabled.foreach( linkSender.sendLinks(_) )
        } yield r
    }

    def submitLink(subreddit: Subreddit, link: Link, maybeFlair: Option[String]): Unit = {
        logger.info("Current linkBean: " + link)

        try{
            maybeFlair
                .map(
                    flair =>  redditSubmitter.submitLink(subreddit.getName, new URL(link.getUrl), link.getTitle, flair)
                )
                .getOrElse( redditSubmitter.submitLink(subreddit.getName, new URL(link.getUrl), link.getTitle) )

            val linkSending = LinkSending(-1, link.getId(),LocalDateTime.now(),subreddit.getId() )
            linkSendingPersistence.save(linkSending)

            if(! subreddit.getModerator){
                waitSomeTime()
            }
        } catch {
            case e: Error => {
                logger.error(e.getMessage(), e)
                waitSomeTime()
            }
        }
    }

    def waitSomeTime() : Unit = {
        logger.info("Waiting some time...");
        try {
            Thread.sleep(10 * 60 * 1000);  // 10 minutes.
        } catch {
            case _:InterruptedException => Thread.currentThread().interrupt();
        }
    }

}