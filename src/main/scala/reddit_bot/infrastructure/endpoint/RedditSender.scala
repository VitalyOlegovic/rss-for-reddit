package reddit_bot.infrastructure.endpoint

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.domain.entity.{Link, LinkSending, Subreddit}
import reddit_bot.infrastructure.repository.{LinkSendingRepository, SubredditRepository}
import reddit_bot.repository.LinkSendingRepository
import reddit_bot.service.LinkSender

import java.net.URL
import java.util.Date
import scala.jdk.CollectionConverters._

@Service
class RedditSender(
    @Autowired subredditRepository: SubredditRepository,
    @Autowired redditSubmitter: RedditSubmitter,
    @Autowired linkSendingRepository: LinkSendingRepository,
    @Autowired linkSender: LinkSender
){
    private val logger = LoggerFactory.getLogger(classOf[RedditSender])

    def send: Unit = {
        subredditRepository.findEnabled()
            .asScala
            .foreach( linkSender.sendLinks(_) )
    }

    def submitLink(subreddit: Subreddit, link: Link, maybeFlair: Option[String]): Unit = {
        logger.info("Current linkBean: " + link)

        try{
            maybeFlair
                .map(
                    flair =>  redditSubmitter.submitLink(subreddit.getName, new URL(link.getUrl), link.getTitle, flair)
                )
                .getOrElse( redditSubmitter.submitLink(subreddit.getName, new URL(link.getUrl), link.getTitle) )

            val linkSending = new LinkSending(link,subreddit, new Date())
            linkSendingRepository.save(linkSending)

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