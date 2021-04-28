package reddit_bot.infrastructure.endpoint;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import reddit_bot.domain.bean.FeedBean;
import reddit_bot.domain.bean.LinkBean;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import reddit_bot.domain.entity.Feed;
import reddit_bot.domain.entity.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class RSSFeedReader {

    private final static Logger logger = LoggerFactory.getLogger(RSSFeedReader.class);

    public List<LinkBean> readFeeds(FeedBean feedBean){

        List<LinkBean> feeds = new ArrayList<LinkBean>();

        try {
            URL url = new URL(feedBean.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

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
        return feeds;
    }

    public List<Link> readFeedItems(Feed feed){
        List<Link> feeds = new ArrayList<Link>();

        URL url = null;

        try {
            url = new URL(feed.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(url));

            List<SyndEntry> entryList = syndFeed.getEntries();

            for(SyndEntry syndEntry : entryList){

                Link link = new Link();
                link.setTitle(syndEntry.getTitle());

                Connection.Response response = null;
                try {
                    response = Jsoup.connect(syndEntry.getLink()).execute();
                    link.setUrl(response.url().toString());
                    if(syndEntry.getTitle().isEmpty()) {
                        link.setTitle(response.parse().title());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    link.setUrl(syndEntry.getLink());
                }

                link.setTitle(StringEscapeUtils.unescapeHtml4(link.getTitle()));
                LocalDateTime ldt = new java.sql.Timestamp(syndEntry.getPublishedDate().getTime()).toLocalDateTime();
                link.setPublicationDate(ldt);
                link.setFeed(feed);

                feeds.add(link);
            }
            return feeds;
        } catch (Exception e) {
            logger.error( "URL: " + url + " message: " + e.getMessage(),e);
        }
        return feeds;
    }

}
