package reddit_bot;

import org.junit.BeforeClass;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.SubredditRepository;

public abstract class AbstractTest {

    protected static ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootStart.class);

    private static SubredditRepository subredditRepository =  configurableApplicationContext.getBean(SubredditRepository.class);

    @BeforeClass
    public static void beforeClass(){
          //configurableApplicationContext = SpringApplication.run(SpringBootStart.class);
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return configurableApplicationContext;
    }

    public static Subreddit getTestSubreddit(){
        return subredditRepository.findOne(5L);
    }

    public static Subreddit getBooksSubreddit(){
        return subredditRepository.findOne(1L);
    }
}
