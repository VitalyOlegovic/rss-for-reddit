package reddit_bot.service

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reddit_bot.entity.Feed
import reddit_bot.repository.{FeedsRepository, LinkRepository, SubredditRepository}
import reddit_bot.rssfeeds.RSSFeedReader

import java.lang.Iterable
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}

@Service
class LinkUpdater(
                  @Autowired linkRepository: LinkRepository,
                  @Autowired subredditRepository: SubredditRepository,
                  @Autowired feedsRepository: FeedsRepository
                ){

    private val logger = LoggerFactory.getLogger(classOf[LinkService])

    private[service] val rssFeedReader = new RSSFeedReader

    def getFeeds(): Iterable[Feed] = {
        subredditRepository
          .findEnabled
          .asScala
          .flatMap(feedsRepository.findBySubreddit(_).asScala)
          .asJava
    }

    def updateFeeds: Unit = {
        getFeeds()
          .asScala
          .flatMap(rssFeedReader.readFeedItems(_).asScala)
          .foreach{
              link => {
                  val links = linkRepository.findByUrl(link.getUrl)
                  if(! links.iterator().hasNext){
                      linkRepository.save(link)
                  }
              }
          }
    }
}