package reddit_bot.service

import java.util.HashSet
import scala.jdk.CollectionConverters._
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

import reddit_bot.infrastructure.repository.LinkSendingPersistence
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.Subreddit
import cats.effect.IO

@Service
class SentLinksCounting(){

    private val linkSendingPersistence= new LinkSendingPersistence(Database.transactor)

    def countLinksSentRecently( subreddit:Subreddit): IO[Option[Int]] = 
        linkSendingPersistence.countLinksSentAfter(subreddit.id, DateUtils.truncate(new Date(), Calendar.DATE))
    

    def feedsSentRecently(subreddit: Subreddit) = {
        subreddit.recentFeedsWindow
        .map(feedsWindow => {
            var windowCalendar = new GregorianCalendar()
            windowCalendar.add(Calendar.DATE, -1 * feedsWindow)
            windowCalendar.getTime
        })
        .map( DateUtils.truncate(_, Calendar.DATE) )
        .map( linkSendingPersistence.feedsSentAfter(subreddit.id, _) ) 
    }

    def feedsSentToday( subreddit: Subreddit )  = 
        linkSendingPersistence.feedsSentAfter( subreddit.id, DateUtils.truncate( new Date(), Calendar.DATE ) )
}