package reddit_bot.service;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Link;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SubredditServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(SubredditServiceTest.class);

    SubredditService subredditService = configurableApplicationContext.getBean(SubredditService.class);

    @Test
    @Disabled
    public void sendLinks(){
        getTestSubreddit().ifPresent(subredditService::sendLinks);
    }

    @Test
    @Disabled
    public void findLinksToSend(){
        Set<Long> feedsSoFar = new HashSet<Long>();
        Iterable<Link> linkIterable = getTestSubreddit().map(
                subreddit -> subredditService.findLinksToSend(subreddit, feedsSoFar))
                .orElse(new ArrayList<>());

        for(Link link : linkIterable){
            logger.info(link.toString());
        }
    }

    @Test
    @Disabled
    public void bannedDomain() throws URISyntaxException, IOException {
        String url = "http://feedproxy.google.com/~r/SocialDigitalKnowledge/~3/WXYxZ6q76xo/";

        Connection.Response response = Jsoup.connect(url).execute();
        logger.info(response.statusCode() + " : " + response.url());

    }

}
