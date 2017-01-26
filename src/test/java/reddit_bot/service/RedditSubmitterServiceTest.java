package reddit_bot.service;

import org.junit.Ignore;
import org.junit.Test;
import reddit_bot.AbstractTest;
import reddit_bot.reddit.RedditSubmitterService;

public class RedditSubmitterServiceTest extends AbstractTest {

    LinkService linkService = configurableApplicationContext.getBean(LinkService.class);
    RedditSubmitterService redditSubmitterService = configurableApplicationContext.getBean(RedditSubmitterService.class);

    @Test
    @Ignore
    public void send(){
        linkService.updateFeeds();
        redditSubmitterService.send();
    }

}
