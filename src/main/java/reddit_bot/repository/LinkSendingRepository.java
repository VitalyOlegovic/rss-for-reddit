package reddit_bot.repository;

import org.springframework.data.repository.CrudRepository;
import reddit_bot.entity.LinkSending;

public interface LinkSendingRepository extends CrudRepository<LinkSending, Long> {
}
