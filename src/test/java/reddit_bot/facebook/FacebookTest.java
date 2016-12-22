package reddit_bot.facebook;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import reddit_bot.AbstractTest;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


public class FacebookTest extends AbstractTest{

    FacebookSubmitterService facebookSubmitterService = configurableApplicationContext.getBean(FacebookSubmitterService.class);
    LinkSendingRepository linkSendingRepository = configurableApplicationContext.getBean(LinkSendingRepository.class);
    SubredditRepository subredditRepository = configurableApplicationContext.getBean(SubredditRepository.class);
    LinkRepository linkRepository = configurableApplicationContext.getBean(LinkRepository.class);

    private String NOTIZIE_CULTURALI = "241098196319795";

    @Test
    public void test() throws MalformedURLException {
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        Subreddit subreddit = subredditRepository.findOne(2L);
        Set<Long> links = linkSendingRepository.linksSentAfter(subreddit, date);

        for(Long linkId : links){
            Link link = linkRepository.findOne(linkId);

            FacebookPost facebookPost = new FacebookPost(new URL(link.getUrl()), link.getTitle(), "", "");
            facebookSubmitterService.submitPostToGroup(NOTIZIE_CULTURALI, facebookPost);
        }
    }

}
