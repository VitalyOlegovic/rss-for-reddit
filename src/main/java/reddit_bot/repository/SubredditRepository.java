package reddit_bot.repository;

import org.springframework.data.jpa.repository.Query;
import reddit_bot.entity.Subreddit;
import org.springframework.data.repository.CrudRepository;

public interface SubredditRepository extends CrudRepository<Subreddit,Long> {

    @Query("select s from Subreddit s where enabled = true")
    public Iterable<Subreddit> findEnabled();

}
