package reddit_bot.service

import cats.effect.{ContextShift, IO}
import cats.implicits._

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.domain.entity.Feed
import reddit_bot.infrastructure.endpoint.RSSFeedReader
import reddit_bot.infrastructure.repository.{FeedsRepository, SubredditRepository, SubredditPersistence}

import java.lang.Iterable
import scala.jdk.CollectionConverters._
import reddit_bot.domain.entity.Link
import scala.util.Try
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.LinkPersistence

@Service
class LinkUpdater(
                  @Autowired subredditRepository: SubredditRepository,
                  @Autowired feedsRepository: FeedsRepository
                ){

    private val logger = LoggerFactory.getLogger(classOf[LinkUpdater])

    private[service] val rssFeedReader = new RSSFeedReader

    def getFeeds(): IO[List[Feed]] = 
        for{
            enabled <- new SubredditPersistence(Database.transactor).findEnabled
            entitiesEnabled = enabled.map(_.toEntity)
            frs = entitiesEnabled.flatMap(feedsRepository.findBySubreddit(_).asScala)
        } yield (frs)
    

    def updateFeeds(): Unit = {
        val linkPersistence = new LinkPersistence(Database.transactor())

        val links: IO[List[Int]] = for{
            feeds <- getFeeds()
            ls = feeds
                .map(x => Try(rssFeedReader.readFeedItems(x)))
                .filter(_.isSuccess)
                .flatMap(_.get.asScala)
                
            
            result <- ls.traverse(
                (link: Link) => 
                    for{
                        foundLinksCount: Option[Int] <- linkPersistence.countByUrl(link.getUrl())
                        maxId <- linkPersistence.maxId()
                        n: Int <- linkPersistence.insert(link, foundLinksCount, maxId)
                        
                    }yield(n)
            )

        }yield(result)
        links.unsafeRunSync()
    }
}