package reddit_bot.facebook;

import org.junit.Ignore;
import org.junit.Test;
import reddit_bot.AbstractTest;

import java.net.MalformedURLException;

public class FacebookTest extends AbstractTest{

    FacebookSubmitterService facebookSubmitterService = configurableApplicationContext.getBean(FacebookSubmitterService.class);

    private String NOTIZIE_CULTURALI = "241098196319795";
    private Long ITALIA_SUBREDDIT_ID = 2L;

    @Test
    @Ignore
    public void test() {
        facebookSubmitterService.submitFromSubreddit(ITALIA_SUBREDDIT_ID, NOTIZIE_CULTURALI);
    }

}
