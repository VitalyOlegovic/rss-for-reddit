package reddit_bot.repository;

import org.springframework.data.repository.CrudRepository;
import reddit_bot.entity.Feed;

public interface FeedsRepository extends CrudRepository<Feed,Long> {
}
