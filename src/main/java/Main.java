import net.dean.jraw.ApiException;
import net.dean.jraw.http.NetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(RSSFeedReader.class);

    public static void main(String ... args){
        LinkDAO linkDAO = new LinkDAO();


        try {
            List<LinkBean> linkBeanList = linkDAO.unpublished();

            if(linkBeanList.size() == 0){
                RSSFeedReader.readFeeds();
                linkBeanList = linkDAO.unpublished();
            }

            RedditSubmitter redditSubmitter = new RedditSubmitter();

            for(LinkBean linkBean : linkBeanList){
                try {
                    redditSubmitter.submitLink(linkBean.getSubreddit(), linkBean.getUrl(), linkBean.getTitle());
                    linkDAO.setSent(linkBean.getId());
                    PersistenceProvider.getInstance().shutdown();
                    waitSomeTime();
                }catch(ApiException ae) {
                    redditSubmitter = new RedditSubmitter();
                    waitSomeTime();
                }catch(NetworkException ne) {
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
        try {
            Thread.sleep(10 * 60 * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
