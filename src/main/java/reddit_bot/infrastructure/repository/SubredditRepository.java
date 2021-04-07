package reddit_bot.infrastructure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reddit_bot.domain.entity.Subreddit;
import org.springframework.data.repository.CrudRepository;

public interface SubredditRepository extends CrudRepository<Subreddit,Long> {

    @Query("select s from Subreddit s where enabled = true order by priority")
    public Iterable<Subreddit> findEnabled();

    @Query("from Subreddit where name = :name")
    public Subreddit getByName(@Param("name") String name);

}
