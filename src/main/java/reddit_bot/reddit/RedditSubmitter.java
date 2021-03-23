package reddit_bot.reddit;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;

import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.*;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthException;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.references.SubmissionFlairReference;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.references.SubredditReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class RedditSubmitter {

    private final static Logger logger = LoggerFactory.getLogger(RedditSubmitter.class);

    private Properties properties;
    RedditClient redditClient;

    public RedditSubmitter(){
        init();
    }

    private void init(){
        try {
            properties = readProperties();

            UserAgent myUserAgent = buildUserAgent();
            NetworkAdapter adapter = new OkHttpNetworkAdapter(myUserAgent);

            Credentials credentials = buildCredentials();
            redditClient = OAuthHelper.automatic(adapter, credentials);
            /*
            OAuthHelper oAuthHelper = redditClient.getAuthManager();
            OAuthData authData = oAuthHelper.easyAuth(credentials);
            redditClient.authenticate(authData);
            fluent = new FluentRedditClient(redditClient);*/
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } catch (OAuthException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void submitLink(String subredditName, URL url, String title) throws ApiException, MalformedURLException {
        try {
            redditClient.subreddit(subredditName).submit(SubmissionKind.LINK, title, url.toString(), true);
        }catch(NetworkException ae){
            logger.error(ae.getMessage(), ae);
            init();
            redditClient.subreddit(subredditName).submit(SubmissionKind.LINK, title, url.toString(), true);
        }
    }

    public void submitLink(String subredditName, URL url, String title, String flair) throws ApiException, MalformedURLException {
        try {
            submitLinkInternal(subredditName, url, title, flair);
        }catch(NetworkException ne){
            logger.error(ne.getMessage(), ne);
            init();
            submitLinkInternal(subredditName, url, title, flair);
        }
    }

    private void submitLinkInternal(String subredditName, URL url, String title, String flair) throws NetworkException, ApiException {
        SubredditReference subredditReference = redditClient.subreddit(subredditName);
        SubmissionReference submissionReference = subredditReference.submit(SubmissionKind.LINK, title, url.toString(), true);

        if(StringUtils.isNotBlank(flair)) {
            submitFlair(subredditReference, submissionReference, flair);
        }
    }

    private void submitFlair(SubredditReference subredditReference, SubmissionReference submissionReference, String flairText)
            throws ApiException {
        List<Flair> flairs = subredditReference.linkFlairOptions();
        Optional<Flair> optionalFlair = flairs.stream().filter(flair -> flair.getText().equals(flairText)).findFirst();
        optionalFlair.ifPresent(
                flair -> {
                    submissionReference.flair(subredditReference.getSubreddit()).updateToTemplate(flair.getId(), flair.getText());
                }
        );
    }

    private UserAgent buildUserAgent(){

        String platform = properties.getProperty("user.agent.platform");
        String appId = properties.getProperty("user.agent.app.id");
        String version = properties.getProperty("user.agent.version");
        String redditUsername = properties.getProperty("user.agent.reddit.username");

        return new UserAgent(platform, appId, version, redditUsername);
    }

    private Credentials buildCredentials(){
        String username = properties.getProperty("credentials.username");
        String password = properties.getProperty("credentials.password");
        String clientId = properties.getProperty("credentials.client.id");
        String clientSecret = properties.getProperty("credentials.client.secret");
        String redirectUrl = properties.getProperty("credentials.redirect.url");

        return Credentials.script(username, password, clientId, clientSecret);
    }

    private Properties readProperties() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("authentication.properties");
        prop.load(stream);
        return prop;
    }

}
