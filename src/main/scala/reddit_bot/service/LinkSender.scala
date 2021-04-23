package reddit_bot.service

import java.net.URL
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.jdk.CollectionConverters._
import reddit_bot.domain.entity.Feed
import reddit_bot.domain.entity.FeedSubreddit
import reddit_bot.domain.entity.Link
import reddit_bot.domain.entity.LinkSending
import reddit_bot.domain.entity.Subreddit
import org.springframework.stereotype.Service
import reddit_bot.domain.dto.SubredditDTO
import reddit_bot.infrastructure.endpoint.RedditSubmitter
import reddit_bot.infrastructure.repository.{FeedSubredditRepository, FeedsRepository, LinkSendingRepository, SubredditRepository}
import reddit_bot.infrastructure.repository.LinkPersistence
import reddit_bot.infrastructure.repository.Database

@Service
class LinkSender(
    @Autowired linkSendingRepository: LinkSendingRepository,
    @Autowired feedsRepository: FeedsRepository,
    @Autowired sentLinksCounting: SentLinksCounting,
    @Autowired redditSubmitter: RedditSubmitter,
    @Autowired feedSubredditRepository: FeedSubredditRepository,
    @Autowired subredditRepository: SubredditRepository
){

    private val logger = LoggerFactory.getLogger(classOf[LinkSender])

    def send = {
        subredditRepository.findEnabled.forEach( sendLinks(_) )
    }

    def sendLinks(subreddit:Subreddit) : Unit = {
        val sentSoFar = sentLinksCounting.countLinksSentRecently(subreddit)
        if(sentSoFar < subreddit.getDailyQuota){
            val feedsSoFar: scala.collection.immutable.Set[Long] = sentLinksCounting.feedsSentRecently(subreddit)
            val links = findLinksToSend(subreddit, feedsSoFar).toSet
            links
                .take(subreddit.getDailyQuota - sentSoFar)
                .foreach{
                    link => {
                        val feedSubreddit = feedSubredditRepository.getFeedSubreddit(subreddit, link.getFeed())
                        submitLink(subreddit, link, Option(feedSubreddit.getFlair()))
                    }
                }
        }
    }

    def findLinksToSend(
        subreddit: Subreddit, feedsSoFar: Set[scala.Long]
        ) : List[Link] = {
        val feeds = feedsRepository.findBySubreddit(subreddit).asScala.toSet
        val notSentFeeds = feeds.map(_.getId).removedAll(feedsSoFar).toList
        val links = new LinkPersistence(Database.transactor()).findByFeedIds( notSentFeeds).unsafeRunSync()
        enforceOneLinkForSource( links.map(_.toEntity) )
    }

    def enforceOneLinkForSource(links: List[Link]): List[Link] = {
        links
            .groupBy(_.getFeed().getId())
            .map( 
                _._2
                    .sortBy( _.getPublicationDate() )
                    .reverse
                    .head 
            )
            .toList
            .sortBy(_.getPublicationDate())
            .reverse
    }

    def subreddits: java.lang.Iterable[SubredditDTO] = {
        subredditRepository
            .findAll
            .asScala
            .map(new SubredditDTO(_))
            .asJava
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