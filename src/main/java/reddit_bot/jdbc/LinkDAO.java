package reddit_bot.jdbc;

import reddit_bot.bean.LinkBean;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LinkDAO {

    private final static Logger logger = LoggerFactory.getLogger(LinkDAO.class);

    public List<LinkBean> unpublished(String subreddit){
        List<LinkBean> linkBeanList = new ArrayList<LinkBean>();
        try {

            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement(
                    "select * from links where subreddit = ? and sent_reddit_date IS NULL order by publication_date desc");
            preparedStatement.setString(1, subreddit);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                LinkBean linkBean = new LinkBean();
                try {
                    linkBean.setId(resultSet.getLong("id"));
                    linkBean.setTitle( resultSet.getString("title") );
                    linkBean.setUrl(new URL(resultSet.getString("url")));
                    linkBean.setSubreddit(resultSet.getString("subreddit"));
                    linkBean.setFeedId(resultSet.getLong("feed_id"));

                    linkBeanList.add(linkBean);
                } catch (MalformedURLException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

        return linkBeanList;
    }

    public void persist(LinkBean linkBean){

        logger.debug(linkBean.toString());

        try {

            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement(
                    "insert into links(title, url, publication_date, subreddit, feed_id, sent_reddit_date) values (?, ?, ?, ?, ?, null)");
            preparedStatement.setString(1, linkBean.getTitle());
            preparedStatement.setString(2, linkBean.getUrl().toExternalForm());
            preparedStatement.setDate(3, new java.sql.Date(linkBean.getPublicationDate().getTime()));
            preparedStatement.setString(4, linkBean.getSubreddit());
            preparedStatement.setLong(5, linkBean.getFeedId());

            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void setSent(Long id){
        PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("update links set sent_reddit_date = ? where id = ?");
        try {
            preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
            preparedStatement.setLong(2,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

    }

    public boolean existsByUrlSubreddit(String url, String subreddit){
        try {
            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("select * from links where url = ? and subreddit = ?");
            preparedStatement.setString(1, url);
            preparedStatement.setString(2, subreddit);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Set<Long> getRecentFeedIds(String subreddit){

        Set<Long> longHashSet = new HashSet<Long>();

        try {
            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("select * from links where subreddit = ? and sent_reddit_date > ?");
            preparedStatement.setString(1, subreddit);
            Date truncatedDate = DateUtils.truncate(new Date(), Calendar.DATE);
            preparedStatement.setDate(2, new java.sql.Date(truncatedDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                longHashSet.add(resultSet.getLong("feed_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return longHashSet;
    }

    public static void main(String ... args) throws MalformedURLException {
        LinkDAO linkDAO = new LinkDAO();
        LinkBean linkBean = new LinkBean();
        linkBean.setTitle("test");
        linkBean.setUrl(new URL("http://www.google.com"));
        linkBean.setPublicationDate(new Date());
        linkBean.setSent(false);
        linkDAO.persist(linkBean);
    }

    public int getRecentLinksCount(String subreddit){

        int recentLinksCount = -1;

        try {
            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement(
                    "select count(1) from links where subreddit = ? and sent_reddit_date >= ?");
            preparedStatement.setString(1, subreddit);
            Date truncatedDate = DateUtils.truncate(new Date(), Calendar.DATE);
            preparedStatement.setDate(2, new java.sql.Date(truncatedDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                recentLinksCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        logger.debug("Recent links count for subreddit " + subreddit + ": " + recentLinksCount );
        return recentLinksCount;
    }

}
