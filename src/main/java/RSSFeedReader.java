import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSFeedReader {

    private final static Logger logger = LoggerFactory.getLogger(RSSFeedReader.class);

    public static List<LinkBean> readFeeds(FeedBean feedBean){

        List<LinkBean> feeds = new ArrayList<LinkBean>();

        try {
            URL url = new URL(feedBean.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            logger.debug(feed.toString());

            List<SyndEntry> entryList = feed.getEntries();

            for(SyndEntry syndEntry : entryList){
                URL linkUrl = new URL(syndEntry.getLink());

                LinkBean linkBean = new LinkBean();
                linkBean.setTitle(syndEntry.getTitle());
                linkBean.setUrl(linkUrl);
                linkBean.setPublicationDate(syndEntry.getPublishedDate());
                linkBean.setSent(false);
                linkBean.setSubreddit(feedBean.getSubreddit());
                linkBean.setFeedId(feedBean.getId());

                feeds.add(linkBean);
            }
            return feeds;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static List<LinkBean> readFeeds() {
        List<LinkBean> list = new ArrayList<LinkBean>();
        FeedDAO feedDAO = new FeedDAO();
        List<FeedBean> feedList = feedDAO.getFeeds();

        LinkDAO linkDAO = new LinkDAO();

        for(FeedBean feedBean : feedList){
            List<LinkBean> links = readFeeds(feedBean);
            for(LinkBean l : links){
                linkDAO.persist(l);
            }
        }

        return list;
    }
}
