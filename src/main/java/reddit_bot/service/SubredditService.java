package reddit_bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reddit_bot.entity.Feed;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.repository.FeedsRepository;
import reddit_bot.repository.LinkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SubredditService {

    private final static Logger logger = LoggerFactory.getLogger(SubredditService.class);

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    FeedsRepository feedsRepository;

    @Autowired
    LinkSendingService linkSendingService;

    @Autowired
    RedditSubmitterService redditSubmitterService;

    public synchronized void sendLinks(Subreddit subreddit){
        Set<Long> feedsSoFar = linkSendingService.feedsSentRecently(subreddit);

        Iterable<Link> links = findLinksToSend(subreddit);

        int sentSoFar = linkSendingService.linksSentRecently(subreddit);
        logger.info(String.format("%s links sent so far for subreddit %s", sentSoFar, subreddit.getName()));

        if(sentSoFar >= subreddit.getDailyQuota()){
            logger.info(String.format(
                    "Daily quota of %s links has been reached for subreddit %s, skipping.",
                    subreddit.getDailyQuota(), subreddit.getName()));
            return;
        }

        for(Link link : links){

            if(feedsSoFar.contains(link.getFeed().getId())){
                continue;
            }

            if(sentSoFar < subreddit.getDailyQuota()) {
                feedsSoFar.add(link.getFeed().getId());
                redditSubmitterService.submitLink(subreddit, link);
                sentSoFar++;
            }else{
                break;
            }
        }
    }

    public Iterable<Link> findLinksToSend(Subreddit subreddit){
        Iterable<Feed> feeds = feedsRepository.findBySubreddit(subreddit);
        List<Long> ids = new ArrayList<Long>();
        for(Feed feed : feeds){
            ids.add(feed.getId());
        }

        if(ids.size() == 0){
            return new ArrayList<Link>();
        }

        return linkRepository.findByFeedIds(ids);
    }

}
