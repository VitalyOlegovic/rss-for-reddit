import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedDAO {

    private final static Logger logger = LoggerFactory.getLogger(FeedDAO.class);

    public List<FeedBean> getFeeds(){

        List<FeedBean> feedBeanList = new ArrayList<FeedBean>();

        try {
            ResultSet resultSet = PersistenceProvider.getInstance().query("select * from feeds");
            while (resultSet.next()){
                FeedBean feedBean = new FeedBean();
                feedBean.setId(resultSet.getLong("id"));
                feedBean.setUrl(resultSet.getString("url"));
                feedBean.setSubreddit(resultSet.getString("subreddit"));
                feedBeanList.add(feedBean);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return feedBeanList;
    }

}
