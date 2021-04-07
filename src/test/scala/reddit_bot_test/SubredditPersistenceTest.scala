package reddit_bot_test

import org.junit.{Ignore, Test}
import org.slf4j.LoggerFactory
import reddit_bot.infrastructure.repository.SubredditPersistence

class SubredditPersistenceTest {

  private val logger = LoggerFactory.getLogger(classOf[SubredditPersistenceTest])

  @Test
  @Ignore
  def listSubreddits = {
    val subredditPersistence = new SubredditPersistence()
    val result = subredditPersistence.read()
    val toPrint = result.map(_.toString).mkString(", ")
    logger.debug(toPrint)
  }

}
