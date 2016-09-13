package reddit_bot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import reddit_bot.entity.Link;
import reddit_bot.entity.Subreddit;

import java.util.List;

public interface LinkRepository extends CrudRepository<Link,Long> {

    @Query("select l from Link l where url = :url")
    public Iterable<Link> findByUrl(@Param("url") String url);

    @Query("select l from Link l " +
            " where  l.feed.id in (:feedIds) " +
            " and (not exists (select ls from LinkSending ls where ls.subreddit = :subreddit and ls.link.id = l.id)) " +
            " order by l.publicationDate desc"
    )
    public Iterable<Link> findByFeedIds(
            @Param("feedIds") List<Long> feedIds,
            @Param("subreddit") Subreddit subreddit);



}
