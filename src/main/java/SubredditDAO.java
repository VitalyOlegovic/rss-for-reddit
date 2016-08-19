import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubredditDAO {

    public List<SubredditBean> getSubreddits(){
        List<SubredditBean> list = new ArrayList<SubredditBean>();
        try {
            ResultSet resultSet = PersistenceProvider.getInstance().query("select * from subreddits order by priority");
            while(resultSet.next()){
                SubredditBean subredditBean = new SubredditBean();
                subredditBean.setId(resultSet.getLong("id"));
                subredditBean.setName(resultSet.getString("name"));
                subredditBean.setDailyQuota(resultSet.getLong("daily_quota"));
                list.add(subredditBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  list;
    }

}
