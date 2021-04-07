package reddit_bot.infrastructure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import reddit_bot.domain.entity.Feed;
import reddit_bot.domain.entity.FeedSubreddit;
import reddit_bot.domain.entity.Subreddit;

public interface FeedSubredditRepository extends CrudRepository<FeedSubreddit, Long> {

    @Query("select feed.id from FeedSubreddit where subreddit = :subreddit")
    public Iterable<Long> getFeeds(@Param("subreddit") Subreddit subreddit);

    @Query("from FeedSubreddit where subreddit = :subreddit and feed = :feed")
    public FeedSubreddit getFeedSubreddit(
            @Param("subreddit") Subreddit subreddit,
            @Param("feed") Feed feed);

}
