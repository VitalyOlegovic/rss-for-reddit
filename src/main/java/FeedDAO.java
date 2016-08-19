import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedDAO {

    private final static Logger logger = LoggerFactory.getLogger(FeedDAO.class);

    public List<FeedBean> getFeeds(String subreddit){

        List<FeedBean> feedBeanList = new ArrayList<FeedBean>();

        try {

            PreparedStatement preparedStatement = PersistenceProvider.getInstance().prepareStatement("select * from feeds where subreddit = ?");
            preparedStatement.setString(1, subreddit);

            ResultSet resultSet = preparedStatement.executeQuery();
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
