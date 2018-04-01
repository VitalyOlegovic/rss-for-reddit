package reddit_bot.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reddit_bot.SpringBootStart;
import reddit_bot.entity.Link;
import reddit_bot.entity.LinkSending;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;
import reddit_bot.service.SubredditService;

import java.net.URL;
import java.util.Date;

@Service
@Scope("singleton")
public class RedditSubmitterService {

    private final static Logger logger = LoggerFactory.getLogger(RedditSubmitterService.class);

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    RedditSubmitter redditSubmitter;

    @Autowired
    LinkSendingRepository linkSendingRepository;

    @Autowired
    SubredditService subredditService;

    public synchronized void send(){
        Iterable<Subreddit> iterable = subredditRepository.findEnabled();
        for(Subreddit subreddit : iterable) {
            subredditService.sendLinks(subreddit);
        }
    }

    public void submitLink(Subreddit subreddit, Link link){
        logger.info("Current linkBean: " + link);

        try {

            URL url = new URL(link.getUrl());
            redditSubmitter.submitLink(subreddit.getName(), url, link.getTitle());

            LinkSending linkSending = new LinkSending(link,subreddit, new Date());
            linkSendingRepository.save(linkSending);

            if(!subreddit.getModerator()) {
                waitSomeTime();
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            waitSomeTime();
        }
    }

    public void submitLink(Subreddit subreddit, Link link, String flair){
        logger.info("Current linkBean: " + link);

        try {

            URL url = new URL(link.getUrl());
            redditSubmitter.submitLink(subreddit.getName(), url, link.getTitle(), flair);

            LinkSending linkSending = new LinkSending(link,subreddit, new Date());
            linkSendingRepository.save(linkSending);

            if(!subreddit.getModerator()) {
                waitSomeTime();
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            waitSomeTime();
        }
    }


    public static void waitSomeTime(){
        logger.info("Waiting some time...");
        try {
            Thread.sleep(10 * 60 * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String ... args){
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootStart.class, args);
        RedditSubmitterService redditSubmitterService = ctx.getBean(RedditSubmitterService.class);
        redditSubmitterService.send();
    }

}
