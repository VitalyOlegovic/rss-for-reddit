package reddit_bot.service;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class SubredditServiceTest extends AbstractTest {

    private final static Logger logger = LoggerFactory.getLogger(SubredditServiceTest.class);

    SubredditService subredditService = configurableApplicationContext.getBean(SubredditService.class);

    @Test
    @Ignore
    public void sendLinks(){
        Subreddit subreddit = getTestSubreddit();
        subredditService.sendLinks(subreddit);
    }

    @Test
    @Ignore
    public void findLinksToSend(){
        Subreddit subreddit = getTestSubreddit();
        Set<Long> feedsSoFar = new HashSet<Long>();
        Iterable<Link> linkIterable = subredditService.findLinksToSend(subreddit, feedsSoFar);
        for(Link link : linkIterable){
            logger.info(link.toString());
        }
    }

    @Test
    @Ignore
    public void bannedDomain() throws URISyntaxException, IOException {
        String url = "http://feedproxy.google.com/~r/SocialDigitalKnowledge/~3/dzdvO-9OZIs/";

        Connection.Response response = Jsoup.connect(url).execute();
        logger.info(response.statusCode() + " : " + response.url());

    }

}
