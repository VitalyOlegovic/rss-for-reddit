import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class RedditSubmitter {

    private final static Logger logger = LoggerFactory.getLogger(RedditSubmitter.class);

    private FluentRedditClient fluent;
    private Properties properties;

    public RedditSubmitter(){
        try {
            properties = readProperties();

            UserAgent myUserAgent = buildUserAgent();
            RedditClient redditClient = new RedditClient(myUserAgent);
            Credentials credentials = buildCredentials();
            OAuthHelper oAuthHelper = redditClient.getOAuthHelper();
            OAuthData authData = oAuthHelper.easyAuth(credentials);
            redditClient.authenticate(authData);
            fluent = new FluentRedditClient(redditClient);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } catch (OAuthException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public static void main(String... args){
        LinkDAO linkDAO = null;
        try {
            linkDAO = new LinkDAO();
            List<LinkBean> linkBeanList = linkDAO.unpublished();
            RedditSubmitter redditSubmitter = new RedditSubmitter();

            for(LinkBean linkBean : linkBeanList){
                redditSubmitter.submitLink("testitaly", linkBean.getUrl(), linkBean.getTitle());
                linkDAO.setSent(linkBean.getId());
                waitNineMinutes();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            PersistenceProvider.getInstance().shutdown();
        }

    }

    public static void waitNineMinutes(){
        try {
            Thread.sleep(10 * 60 * 1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            PersistenceProvider.getInstance().shutdown();
        }
    }

    public void submitLink(String subredditName, URL url, String title) throws ApiException, MalformedURLException {

        fluent.subreddit(subredditName).submit(url, title);
    }

    private UserAgent buildUserAgent(){

        String platform = properties.getProperty("user.agent.platform");
        String appId = properties.getProperty("user.agent.app.id");
        String version = properties.getProperty("user.agent.version");
        String redditUsername = properties.getProperty("user.agent.reddit.username");

        return UserAgent.of(platform, appId, version, redditUsername);
    }

    private Credentials buildCredentials(){
        String username = properties.getProperty("credentials.username");
        String password = properties.getProperty("credentials.password");
        String clientId = properties.getProperty("credentials.client.id");
        String clientSecret = properties.getProperty("credentials.client.secret");
        String redirectUrl = properties.getProperty("credentials.redirect.url");

        return Credentials.script(username, password, clientId, clientSecret, redirectUrl);
    }

    private Properties readProperties() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("authentication.properties");
        prop.load(stream);
        return prop;
    }

}
