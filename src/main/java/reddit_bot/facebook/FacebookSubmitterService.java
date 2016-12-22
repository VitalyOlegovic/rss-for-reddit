package reddit_bot.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.PostUpdate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
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

@Service
@Scope("singleton")
public class FacebookSubmitterService {

    private final static Logger logger = LoggerFactory.getLogger(FacebookSubmitterService.class);

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    LinkSendingRepository linkSendingRepository;

    @Autowired
    LinkRepository linkRepository;

    public void submitPostToGroup(String groupId, FacebookPost facebookPost){
        Facebook facebook = new FacebookFactory().getInstance();
        try {
            PostUpdate postUpdate = new PostUpdate(facebookPost.getUrl());
            postUpdate.name(facebookPost.getName());
            postUpdate.description(facebookPost.getDescription());
            postUpdate.message(facebookPost.getMessage());
            facebook.postGroupFeed(groupId, postUpdate);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public void submitFromSubreddit(Long sourceSubredditId, String destinationGroupId){
        Date date = DateUtils.truncate(new Date(), Calendar.DATE);
        Subreddit subreddit = subredditRepository.findOne(sourceSubredditId);
        Set<Long> links = linkSendingRepository.linksSentAfter(subreddit, date);

        for(Long linkId : links){
            Link link = linkRepository.findOne(linkId);

            URL url = null;
            try{
                url = new URL(link.getUrl());
                FacebookPost facebookPost = new FacebookPost(url, link.getTitle(), "", "");
                submitPostToGroup(destinationGroupId, facebookPost);
            }catch (MalformedURLException mue){
                logger.error(mue.getLocalizedMessage(), mue);
            }


        }
    }

}
