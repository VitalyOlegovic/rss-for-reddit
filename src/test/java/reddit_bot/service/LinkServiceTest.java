package reddit_bot.service;

import org.junit.Test;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Subreddit;

public class LinkServiceTest extends AbstractTest{

    LinkService linkService = configurableApplicationContext.getBean(LinkService.class);

    @Test
    public void updateFeeds(){
        linkService.updateFeeds();
    }

}
