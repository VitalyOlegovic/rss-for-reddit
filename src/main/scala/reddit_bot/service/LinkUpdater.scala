package reddit_bot.service

import cats.effect.{ContextShift, IO}
import cats.implicits._

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.domain.entity.Feed
import reddit_bot.infrastructure.endpoint.RSSFeedReader
import reddit_bot.infrastructure.repository.{FeedsRepository, LinkRepository, SubredditRepository, SubredditPersistence}

import java.lang.Iterable
import scala.jdk.CollectionConverters._
import reddit_bot.domain.entity.Link
import scala.util.Try
import reddit_bot.infrastructure.repository.Database

@Service
class LinkUpdater(
                  @Autowired linkRepository: LinkRepository,
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
        val links = for{
            feeds <- getFeeds()
            ls: List[Link] = feeds.flatMap(rssFeedReader.readFeedItems(_).asScala)
            
            ios = ls.map(
                link => 
                    IO{
                        val foundLinks = linkRepository
                            .findByUrl(link.getUrl())
                            .asScala
                            
                        if(foundLinks.size == 0){
                            Try(linkRepository.save(link))
                        }
                        link.getUrl()
                    }
            )

            s <- ios.sequence
        }yield(s)
        links.unsafeRunSync()
    }
}