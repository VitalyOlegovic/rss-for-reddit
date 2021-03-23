package reddit_bot.service;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reddit_bot.AbstractTest;
import reddit_bot.reddit.RedditSubmitterService;

public class RedditSubmitterServiceTest extends AbstractTest {

    LinkService linkService = configurableApplicationContext.getBean(LinkService.class);
    RedditSubmitterService redditSubmitterService = configurableApplicationContext.getBean(RedditSubmitterService.class);

    @Test
    @Disabled
    public void send(){
        //linkService.updateFeeds();
        redditSubmitterService.send();
    }

}
