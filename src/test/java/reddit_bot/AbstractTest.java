package reddit_bot;


import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import reddit_bot.entity.Subreddit;
import reddit_bot.repository.SubredditRepository;

import java.util.Optional;

public abstract class AbstractTest {

    protected static ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootStart.class);

    private static SubredditRepository subredditRepository =  configurableApplicationContext.getBean(SubredditRepository.class);

    @BeforeAll
    public static void beforeClass(){
          //configurableApplicationContext = SpringApplication.run(SpringBootStart.class);
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return configurableApplicationContext;
    }

    public static Optional<Subreddit> getTestSubreddit(){
        return subredditRepository.findById(5L);
    }

    public static Optional<Subreddit> getBooksSubreddit(){
        return subredditRepository.findById(1L);
    }
}
