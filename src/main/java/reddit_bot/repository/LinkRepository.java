package reddit_bot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import reddit_bot.entity.Link;

public interface LinkRepository extends CrudRepository<Link,Long> {

    @Query("select l from Link l where url = :url")
    public Iterable<Link> findByUrl(@Param("url") String url);

}
