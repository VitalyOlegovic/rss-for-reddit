package reddit_bot.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Subreddit;

import java.util.Set;

public class LinkSendingServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(LinkSendingServiceTest.class);

    LinkSendingService linkSendingService = configurableApplicationContext.getBean(LinkSendingService.class);

    @Test
    public void linksSentRecently(){
        Subreddit subreddit = getTestSubreddit();
        int i = linkSendingService.linksSentRecently(subreddit);
        logger.info("Number of links sent recently: " + i);
    }

    @Test
    public void feedsSentRecently(){
        Subreddit subreddit = getTestSubreddit();
        Set<Long> longSet = linkSendingService.feedsSentRecently(subreddit);
        logger.info("Feeds sent recently: " + longSet.toString());
    }

}