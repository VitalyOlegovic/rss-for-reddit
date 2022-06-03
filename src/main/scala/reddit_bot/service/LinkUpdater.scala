package reddit_bot.service

import cats.effect.{ContextShift, IO}
import cats.implicits._

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service
import reddit_bot.infrastructure.endpoint.RSSFeedReader

import scala.jdk.CollectionConverters._
import reddit_bot.domain.entity.Link
import scala.util.Try
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.LinkPersistence
import reddit_bot.infrastructure.repository.SubredditPersistence
import reddit_bot.infrastructure.repository.FeedPersistence

@Service
class LinkUpdater(

                ){

    private val logger = LoggerFactory.getLogger(classOf[LinkUpdater])

    private[service] val rssFeedReader = new RSSFeedReader
    val linkPersistence = new LinkPersistence(Database.transactor)
    val feedPersistence = new FeedPersistence(Database.transactor)


    def getFeeds(): IO[List[feedPersistence.Feed]] = 
        for{
            enabled <- new SubredditPersistence(Database.transactor).findEnabled
            entitiesEnabled = enabled.map(_.toEntity)
            frs <- entitiesEnabled.flatTraverse(x=>feedPersistence.findBySubredditId(x.getId()))
        } yield (frs)
    

    def updateFeeds(): Unit = {
        
        val links: IO[List[Option[Int]]] = for{
            feeds <- getFeeds()
            ls = feeds
                .map(x => Try(rssFeedReader.readFeedItems(x)))
                .filter(_.isSuccess)
                .flatMap(_.get.asScala)
                
            
            result <- ls.traverse(
                (link: Link) => 
                    for{
                        foundLinksCount: Int <- linkPersistence.countByUrl(link.getUrl())
                        maxId <- linkPersistence.maxId()
                        n: Option[Int] <- insertIfNotPresent(link, foundLinksCount, maxId)
                        
                    }yield(n)
            )

        }yield(result)
        links.unsafeRunSync()
    }

    def insertIfNotPresent(link: Link, foundLinksCount: Int, maxId: Int): IO[Option[Int]] = 
        foundLinksCount match{
            case 0 => linkPersistence.insert(link, maxId).map(Option(_))
            case _ => IO.pure(None)
        }
    
}