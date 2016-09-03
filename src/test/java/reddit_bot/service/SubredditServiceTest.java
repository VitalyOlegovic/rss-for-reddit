package reddit_bot.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;

import java.util.HashSet;
import java.util.Set;

public class SubredditServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(SubredditServiceTest.class);

    SubredditService subredditService = configurableApplicationContext.getBean(SubredditService.class);

    @Test
    public void sendLinks(){
        Subreddit subreddit = getTestSubreddit();
        subredditService.sendLinks(subreddit);
    }

    @Test
    public void findLinksToSend(){
        Subreddit subreddit = getTestSubreddit();
        Set<Long> feedsSoFar = new HashSet<Long>();
        Iterable<Link> linkIterable = subredditService.findLinksToSend(subreddit, feedsSoFar);
        for(Link link : linkIterable){
            logger.info(link.toString());
        }
    }

}
