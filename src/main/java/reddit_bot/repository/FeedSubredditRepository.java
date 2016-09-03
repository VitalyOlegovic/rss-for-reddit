package reddit_bot.repository;

import org.springframework.data.jpa.repository.Query;
import reddit_bot.entity.Subreddit;

public interface FeedSubredditRepository {

    @Query("select feed.id from FeedSubreddit where subreddit = :subreddit")
    public Iterable<Long> getFeeds(Subreddit subreddit);

}
