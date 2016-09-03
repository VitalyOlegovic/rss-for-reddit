package reddit_bot.reddit;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.managers.ModerationManager;
import net.dean.jraw.models.FlairTemplate;
import net.dean.jraw.models.Submission;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

@Service
public class RedditSubmitter {

    private final static Logger logger = LoggerFactory.getLogger(RedditSubmitter.class);

    private FluentRedditClient fluent;
    private Properties properties;
    RedditClient redditClient;

    public RedditSubmitter(){
        init();
    }

    private void init(){
        try {
            properties = readProperties();

            UserAgent myUserAgent = buildUserAgent();
            redditClient = new RedditClient(myUserAgent);
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

    public void submitLink(String subredditName, URL url, String title) throws ApiException, MalformedURLException {
        try {
            Submission submission = fluent.subreddit(subredditName).submit(url, title);
        }catch(ApiException ae){
            init();
            throw ae;
        }
    }

    public void submitLink(String subredditName, URL url, String title, String flair) throws ApiException, MalformedURLException {
        try {
            Submission submission = fluent.subreddit(subredditName).submit(url, title);

            if(StringUtils.isNotBlank(flair)) {
                submitFlair(subredditName, submission, flair);
            }

        }catch(ApiException ae){
            init();
            throw ae;
        }
    }

    private void submitFlair(String subredditName, Submission submission, String flair) throws ApiException {
        AccountManager accountManager = new AccountManager(redditClient);
        List<FlairTemplate> flairTemplateList = accountManager.getFlairChoices(submission);

        ModerationManager moderationManager = new ModerationManager(redditClient);

        FlairTemplate flairTemplate = null;

        for (FlairTemplate ft : flairTemplateList) {
            if (ft.getText().equalsIgnoreCase(flair)) {
                logger.info("Flair found");
                flairTemplate = ft;
            }
        }

        if (flairTemplate != null) {
            moderationManager.setFlair(subredditName, flairTemplate, flair, submission);
        }
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
