package reddit_bot.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.stereotype.Service;
import reddit_bot.bean.FeedBean;
import reddit_bot.bean.LinkBean;
import reddit_bot.entity.Feed;
import reddit_bot.entity.FeedSubreddit;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;
import reddit_bot.jdbc.FeedDAO;
import reddit_bot.jdbc.LinkDAO;
import reddit_bot.repository.FeedsRepository;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.SubredditRepository;
import reddit_bot.rssfeeds.RSSFeedReader;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LinkService {

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    FeedsRepository feedsRepository;

    RSSFeedReader rssFeedReader = new RSSFeedReader();


    public Iterable<Feed> getFeeds(){
        Iterable<Subreddit> iterable = subredditRepository.findEnabled();

        Set<Feed> feedSet = new HashSet<Feed>();

        for(Subreddit subreddit : iterable){
            for(Feed feed : feedsRepository.findBySubreddit(subreddit) ){
                feedSet.add(feed);
            }
        }

        return feedSet;
    }

    public void updateFeeds(){
        Iterable<Feed> feeds = getFeeds();
        for(Feed feed : feeds){
            List<Link> linkList = rssFeedReader.readFeedItems(feed);
            for(Link link : linkList) {
                Iterable<Link> links = linkRepository.findByUrl(link.getUrl());
                if(! links.iterator().hasNext()) {
                    String title = StringEscapeUtils.unescapeHtml4(link.getTitle());
                    link.setTitle(title);
                    linkRepository.save(link);
                }
            }
        }
    }

}
