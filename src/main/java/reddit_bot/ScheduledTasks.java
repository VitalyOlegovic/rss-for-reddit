package reddit_bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.service.LinkService;
import reddit_bot.service.SubredditService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    @Autowired
    LinkService linkService;

    @Autowired
    RedditSubmitterService redditSubmitterService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 4 21 1/1 * ?")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 53 17 1/1 * ?")
    public void updateFeeds(){
        linkService.updateFeeds();
    }

    @Scheduled(cron = "0 55 17 1/1 * ?")
    public void send(){
        redditSubmitterService.send();
    }

}
