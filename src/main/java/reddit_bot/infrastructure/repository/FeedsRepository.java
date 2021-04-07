package reddit_bot.infrastructure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import reddit_bot.domain.entity.Feed;
import reddit_bot.domain.entity.Subreddit;

public interface FeedsRepository extends CrudRepository<Feed,Long> {

    @Query("select f from Feed f join f.feedSubreddits fs where fs.feed.id = f.id and fs.subreddit = :subreddit")
    Iterable<Feed> findBySubreddit(@Param("subreddit")Subreddit subreddit);

}
