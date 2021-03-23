package reddit_bot.service;



import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Subreddit;

import java.util.Set;

public class LinkSendingServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(LinkSendingServiceTest.class);

    LinkSendingService linkSendingService = configurableApplicationContext.getBean(LinkSendingService.class);

    @Test
    @Disabled
    public void linksSentRecently(){
        getTestSubreddit()
                .map(subreddit -> linkSendingService.countLinksSentRecently(subreddit))
                .ifPresent(i -> logger.info("Number of links sent recently: " + i));
    }

    @Test
    @Disabled
    public void feedsSentRecently(){
        getTestSubreddit()
                .map(subreddit -> linkSendingService.feedsSentRecently(subreddit))
                .ifPresent(longSet -> logger.info("Feeds sent recently: " + longSet.toString()));
    }

    @Test
    @Disabled
    public void feedsBooksSentRecently(){
        getBooksSubreddit()
                .map(subreddit -> linkSendingService.feedsSentRecently(subreddit))
                .ifPresent(longSet ->  logger.info("Feeds sent recently: " + longSet.toString()));
    }

}
