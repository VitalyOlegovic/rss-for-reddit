package reddit_bot.service

import java.util.HashSet
import scala.jdk.CollectionConverters._

import org.apache.commons.lang3.time.DateUtils
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.repository.LinkSendingRepository

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

import reddit_bot.entity.Subreddit

@Service
class SentLinksCounting(@Autowired linkSendingRepository: LinkSendingRepository){

    def countLinksSentRecently( subreddit:Subreddit):Int = 
        linkSendingRepository.countLinksSentAfter(subreddit, DateUtils.truncate(new Date(), Calendar.DATE))
    

    def feedsSentRecently(subreddit: Subreddit): Set[Long] = {
        Option(subreddit.getRecentFeedsWindow())
        .map(feedsWindow => {
            var windowCalendar = new GregorianCalendar()
            windowCalendar.add(Calendar.DATE, -1 * feedsWindow)
            windowCalendar.getTime
        })
        .map( DateUtils.truncate(_, Calendar.DATE) )
        .map( linkSendingRepository.feedsSentAfter(subreddit, _).asScala.map(Long2long(_) ).toSet )
        .getOrElse( Set[Long]() )
    }

    def feedsSentToday( subreddit: Subreddit ) : Set[Long] = 
        linkSendingRepository.feedsSentAfter( subreddit, DateUtils.truncate( new Date(), Calendar.DATE ) ).asScala.map(Long2long).toSet
}