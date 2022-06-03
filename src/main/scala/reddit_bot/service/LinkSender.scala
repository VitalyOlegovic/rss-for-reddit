package reddit_bot.service

import java.net.URL
import java.util.Date
import java.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import cats._
import cats.data._
import cats.effect._
import cats.implicits._

import scala.jdk.CollectionConverters._
import reddit_bot.domain.entity
import org.springframework.stereotype.Service
import reddit_bot.domain.dto.SubredditDTO
import reddit_bot.infrastructure.endpoint.RedditSubmitter
import reddit_bot.infrastructure.repository.LinkPersistence
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.FeedPersistence
import scala.util.Try
import reddit_bot.infrastructure.repository.Link
import reddit_bot.infrastructure.repository.FeedSubredditPersistence
import reddit_bot.infrastructure.repository.LinkSending
import reddit_bot.infrastructure.repository.LinkSendingPersistence
import reddit_bot.infrastructure.repository.Subreddit
import reddit_bot.infrastructure.repository.SubredditPersistence
import scala.util.Failure
import scala.util.Success

@Service
class LinkSender(
    @Autowired sentLinksCounting: SentLinksCounting,
    @Autowired redditSubmitter: RedditSubmitter,
){

    private val logger = LoggerFactory.getLogger(classOf[LinkSender])
    private val feedPersistence = new FeedPersistence(Database.transactor)
    private val linkPersistence = new LinkPersistence(Database.transactor)
    private val feedSubredditPersistence = new FeedSubredditPersistence(Database.transactor)
    private val linkSendingPersistence = new LinkSendingPersistence(Database.transactor)
    private val subredditPersistence = new SubredditPersistence(Database.transactor)

    def LinkSending = {
        val r = for {
            enabled: List[Subreddit] <- subredditPersistence.findEnabled
            result = enabled.map( x => sendLinks(x) )
        } yield result
        r.unsafeRunSync
    }
    

    def sendLinks(subreddit:Subreddit): Option[List[Try[Int]]] = {
        val sentSoFar = sentLinksCounting.countLinksSentRecently(subreddit)
        val r = if(sentSoFar < subreddit.dailyQuota){
            val feedsSoFar: scala.collection.immutable.Set[Long] = sentLinksCounting.feedsSentRecently(subreddit)
            logger.info("Feeds so far: " + sentSoFar + " daily quota: " + subreddit.getDailyQuota)
            val r = for{
                links: List[Link] <- findLinksToSend(subreddit, feedsSoFar)
                linksToSend = links.take(subreddit.dailyQuota - sentSoFar)
                result <- linksToSend.traverse{
                        link => for{
                            feedSubreddit <- feedSubredditPersistence.getFeedSubreddit(subreddit.getId(), link.feed_id)
                            result = submitLink(subreddit, link, feedSubreddit.flair)
                        } yield result
                    }
            }yield result
            Some(r.unsafeRunSync())
        }else{
            None
        }

        r
    }

    def findLinksToSend(
        subreddit: Subreddit, feedsSoFar: Set[scala.Long]
        ): IO[List[Link]]  = {
            for{
                feeds <- feedPersistence.findBySubredditId(subreddit.id)
                notSentFeeds = feeds.map(_.id).toSet.removedAll(feedsSoFar).toList
                links: List[Link] <- linkPersistence.findNotSentByFeedIds( notSentFeeds)
                oneForSourceLinks = enforceOneLinkForSource(links)            
            } yield oneForSourceLinks
    }

    def enforceOneLinkForSource(links: List[Link]): List[Link] = {
        links
            .groupBy(_.feed_id)
            .map( 
                _._2
                    .sortBy( _.publication_date )
                    .reverse
                    .head 
            )
            .toList
            .sortBy(_.publication_date)
            .reverse
    }



    def submitLink(subreddit: entity.Subreddit, link: Link, maybeFlair: Option[String]): Try[Int] = {
        logger.info("Current linkBean: " + link)

        val result: Try[Int] = Try{
            maybeFlair
                .map(
                    flair =>  redditSubmitter.submitLink(subreddit.getName, new URL(link.url), link.title, flair)
                )
                .getOrElse( redditSubmitter.submitLink(subreddit.getName, new URL(link.url), link.title) )

            val linkSending = new LinkSending(link.id,LocalDateTime.now(),subreddit.getId())
            
            val io = for{
                r: Int <- linkSendingPersistence.save(linkSending)
            } yield r

            val res = io.unsafeRunSync()

            if(! subreddit.getModerator){
                waitSomeTime()
            }

            res
        }

        logger.info(result.toString())

        result
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