package reddit_bot.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reddit_bot.entity.Feed;
import reddit_bot.entity.Link;
import reddit_bot.entity.LinkSending;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.FeedsRepository;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;

import java.net.URL;
import java.util.*;

@Service
public class RedditService {

    private final static Logger logger = LoggerFactory.getLogger(RedditService.class);

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    FeedsRepository feedsRepository;

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    RedditSubmitter redditSubmitter;

    @Autowired
    LinkSendingRepository linkSendingRepository;

    public void send(){
        Iterable<Subreddit> iterable = subredditRepository.findEnabled();
        for(Subreddit subreddit : iterable) {

            Set<Long> feedsSoFar = new HashSet<Long>();

            Iterable<Link> links = findLinksToSend(subreddit);

            int sentSoFar = 0;

            for(Link link : links){

                if(feedsSoFar.contains(link.getFeed().getId())){
                    continue;
                }

                if(sentSoFar < subreddit.getDailyQuota()) {
                    feedsSoFar.add(link.getFeed().getId());
                    submitLink(subreddit, link);
                    sentSoFar++;
                }else{
                    break;
                }
            }
        }
    }

    private Iterable<Link> findLinksToSend(Subreddit subreddit){
        Iterable<Feed> feeds = feedsRepository.findBySubreddit(subreddit);
        List<Long> ids = new ArrayList<Long>();
        for(Feed feed : feeds){
            ids.add(feed.getId());
        }

        if(ids.size() == 0){
            return new ArrayList<Link>();
        }

        return linkRepository.findByFeedIds(ids, subreddit);
    }

    private void submitLink(Subreddit subreddit, Link link){
        logger.debug("Current linkBean: " + link);

        try {
            URL url = new URL(link.getUrl());
            redditSubmitter.submitLink(subreddit.getName(), url, link.getTitle());

            LinkSending linkSending = new LinkSending(link,subreddit, new Date());
            linkSendingRepository.save(linkSending);

            waitSomeTime();
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            waitSomeTime();
        }
    }

    public static void waitSomeTime(){
        logger.info("Waiting some time...");
        try {
            Thread.sleep(10 * 60 * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
