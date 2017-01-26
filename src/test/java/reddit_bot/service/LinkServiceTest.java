package reddit_bot.service;

import org.junit.Ignore;
import org.junit.Test;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Subreddit;

public class LinkServiceTest extends AbstractTest{

    LinkService linkService = configurableApplicationContext.getBean(LinkService.class);

    @Test
    @Ignore
    public void updateFeeds(){
        linkService.updateFeeds();
    }

}
