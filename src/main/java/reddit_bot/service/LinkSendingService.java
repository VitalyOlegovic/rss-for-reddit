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
        return linkSendingRepository.feedsSentAfter(subreddit, date);
    }

}
