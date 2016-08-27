package reddit_bot;

import reddit_bot.bean.LinkBean;
import reddit_bot.bean.SubredditBean;
import reddit_bot.jdbc.LinkDAO;
import reddit_bot.jdbc.PersistenceProvider;
import reddit_bot.jdbc.SubredditDAO;
import net.dean.jraw.ApiException;
import net.dean.jraw.http.NetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reddit_bot.rssfeeds.RSSFeedReader;
import reddit_bot.rssfeeds.RSSService;

import java.util.List;
import java.util.Set;

public class OrchestratorJob{

    private final static Logger logger = LoggerFactory.getLogger(OrchestratorJob.class);

    private RedditSubmitter redditSubmitter;

    public OrchestratorJob(){
        redditSubmitter = new RedditSubmitter();
    }

    public void submitLinks(){
        SubredditDAO subredditDAO = new SubredditDAO();
        List<SubredditBean> list = subredditDAO.getSubreddits();
        logger.debug(list.toString());

        for(SubredditBean subredditBean : list){
            submitLinks(subredditBean);
        }
    }

    public void submitLinks(SubredditBean subredditBean){
        LinkDAO linkDAO = new LinkDAO();

        if(linkDAO.getRecentLinksCount(subredditBean.getName()) >= subredditBean.getDailyQuota()){
            return;
        }

        try {
            RSSService rssService = new RSSService();
            rssService.readFeeds(subredditBean.getName());
            List<LinkBean> linkBeanList = linkDAO.unpublished(subredditBean.getName());

            for(LinkBean linkBean : linkBeanList){

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

                submitLink(subredditBean, linkBean, recentLinksCount);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void submitLink(SubredditBean subredditBean, LinkBean linkBean, int recentLinksCount){

        LinkDAO linkDAO = new LinkDAO();

        logger.debug("Current linkBean: " + linkBean);


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

    public static void waitSomeTime(){
        logger.info("Waiting some time...");
        try {
            Thread.sleep(10 * 60 * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
