package reddit_bot.service

import org.springframework.beans.factory.annotation.Autowired;
import scala.jdk.CollectionConverters._

import reddit_bot.dto.SubredditDTO;
import reddit_bot.entity.Feed;
import reddit_bot.entity.FeedSubreddit;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.repository.FeedSubredditRepository;
import reddit_bot.repository.FeedsRepository;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.SubredditRepository;

class LinkSender(
    @Autowired linkRepository: LinkRepository,
    @Autowired feedsRepository: FeedsRepository,
    @Autowired sentLinksCounting: SentLinksCounting,
    @Autowired redditSubmitterService: RedditSubmitterService,
    @Autowired feedSubredditRepository: FeedSubredditRepository,
    @Autowired subredditRepository: SubredditRepository
){

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
                        redditSubmitterService.submitLink(subreddit, link, feedSubreddit.getFlair())
                    }
                }
        }
    }

    def findLinksToSend(
        subreddit: Subreddit, feedsSoFar: Set[scala.Long]
        ) : List[Link] = {
        val feeds = feedsRepository.findBySubreddit(subreddit).asScala.toSet
        val notSentFeeds = feeds.map(_.getId).removedAll(feedsSoFar).map( long2Long(_) ).toList.asJava
        linkRepository.findByFeedIds( notSentFeeds, subreddit  ).asScala.toList
    }

    def subreddits: java.lang.Iterable[SubredditDTO] = {
        subredditRepository
            .findAll
            .asScala
            .map(new SubredditDTO(_))
            .asJava
    }
}