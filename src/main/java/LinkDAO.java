import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LinkDAO {

    private final static Logger logger = LoggerFactory.getLogger(LinkDAO.class);

    public List<LinkBean> unpublished(){
        List<LinkBean> linkBeanList = new ArrayList<LinkBean>();
        try {
            ResultSet resultSet = PersistenceProvider.getInstance().query("select * from links where published = FALSE order by publication_date desc");
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

            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("insert into links(title, url, publication_date, published, subreddit, feed_id) values (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, linkBean.getTitle());
            preparedStatement.setString(2, linkBean.getUrl().toExternalForm());
            preparedStatement.setDate(3, new java.sql.Date(linkBean.getPublicationDate().getTime()));
            preparedStatement.setBoolean(4, false);
            preparedStatement.setString(5, linkBean.getSubreddit());
            preparedStatement.setLong(6, linkBean.getFeedId());

            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void setSent(Long id){
        PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("update links set published = true where id = ?");
        try {
            preparedStatement.setLong(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }

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

}
