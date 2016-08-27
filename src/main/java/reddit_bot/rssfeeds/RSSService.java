package reddit_bot.rssfeeds;

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
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.SubredditRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RSSService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    LinkRepository linkRepository;

    RSSFeedReader rssFeedReader = new RSSFeedReader();

    public void readFeeds(String subreddit) {
        List<LinkBean> list = new ArrayList<LinkBean>();
        FeedDAO feedDAO = new FeedDAO();
        List<FeedBean> feedList = feedDAO.getFeeds(subreddit);

        LinkDAO linkDAO = new LinkDAO();

        for(FeedBean feedBean : feedList){
            List<LinkBean> links = rssFeedReader.readFeeds(feedBean);
            for(LinkBean l : links){
                if(!linkDAO.existsByUrlSubreddit(l.getUrl().toExternalForm(),l.getSubreddit())) {
                    linkDAO.persist(l);
                }
            }
        }
    }

    public Iterable<Feed> getFeeds(){
        RepositoryFactorySupport factory = new JpaRepositoryFactory(entityManager);
        SubredditRepository subredditRepository = factory.getRepository(SubredditRepository.class);
        Iterable<Subreddit> iterable = subredditRepository.findEnabled();

        Set<Feed> feedSet = new HashSet<Feed>();

        for(Subreddit subreddit : iterable){
            for(FeedSubreddit feedSubreddit : subreddit.getFeedSubreddits() ){
                feedSet.add(feedSubreddit.getFeed());
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
                    linkRepository.save(link);
                }
            }
        }
    }

}
