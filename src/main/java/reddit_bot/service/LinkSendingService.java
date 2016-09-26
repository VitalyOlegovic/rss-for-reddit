package reddit_bot.service;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.LinkSendingRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

@Service
public class LinkSendingService {

    private final static Logger logger = LoggerFactory.getLogger(SubredditService.class);

    @Autowired
    LinkSendingRepository linkSendingRepository;

    public int linksSentRecently(Subreddit subreddit){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        return linkSendingRepository.linksSentAfter(subreddit, date);
    }

    public Set<Long> feedsSentRecently(Subreddit subreddit){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        Calendar calendar = new GregorianCalendar();

        Integer feedsWindow = subreddit.getRecentFeedsWindow();
        if(feedsWindow != null && feedsWindow > 0) {
            calendar.add(Calendar.DATE, -1 * subreddit.getRecentFeedsWindow());
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return linkSendingRepository.feedsSentAfter(subreddit, calendar.getTime());
    }

}
