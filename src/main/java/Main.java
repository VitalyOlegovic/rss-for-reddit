import net.dean.jraw.ApiException;
import net.dean.jraw.http.NetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String ... args){
        SubredditDAO subredditDAO = new SubredditDAO();
        List<SubredditBean> list = subredditDAO.getSubreddits();
        logger.debug(list.toString());

        for(SubredditBean subredditBean : list){
            submitLinks(subredditBean);
        }
    }

    private static void submitLinks(SubredditBean subredditBean){
        LinkDAO linkDAO = new LinkDAO();

        if(linkDAO.getRecentLinksCount(subredditBean.getName()) >= subredditBean.getDailyQuota()){
            return;
        }

        try {

            RSSFeedReader.readFeeds(subredditBean.getName());
            List<LinkBean> linkBeanList = linkDAO.unpublished(subredditBean.getName());

            RedditSubmitter redditSubmitter = new RedditSubmitter();

            for(LinkBean linkBean : linkBeanList){

                logger.debug("Current linkBean: " + linkBean);

                int recentLinksCount = linkDAO.getRecentLinksCount(subredditBean.getName());
                if(recentLinksCount >= subredditBean.getDailyQuota()){
                    logger.info("Reached daily quota for subreddit " + subredditBean.getName());
                    return;
                }

                Set<Long> recentFeedsIds = linkDAO.getRecentFeedIds(subredditBean.getName());
                if(recentFeedsIds.contains(linkBean.getFeedId())){
                    logger.info("Feed already submitted today, skipping");
                    continue;
                }

                try {
                    redditSubmitter.submitLink(linkBean.getSubreddit(), linkBean.getUrl(), linkBean.getTitle());
                    linkDAO.setSent(linkBean.getId());
                    PersistenceProvider.getInstance().shutdown();

                    if(recentLinksCount +1 >= subredditBean.getDailyQuota()){
                        logger.info("Reached daily quota for subreddit " + subredditBean.getName());
                        return;
                    }

                    waitSomeTime();
                }catch(ApiException ae) {
                    logger.error(ae.getMessage(), ae);
                    redditSubmitter = new RedditSubmitter();
                    waitSomeTime();
                }catch(NetworkException ne) {
                    logger.error(ne.getMessage(), ne);
                    redditSubmitter = new RedditSubmitter();
                    waitSomeTime();
                }catch(Exception e){
                    logger.error(e.getMessage(), e);
                    waitSomeTime();
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void waitSomeTime(){
        logger.info("Waiting some time...");
        try {
            Thread.sleep(10 * 60 * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
