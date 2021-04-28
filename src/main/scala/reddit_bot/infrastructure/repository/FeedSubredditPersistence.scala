package reddit_bot.infrastructure.repository

import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.implicits.javatime._ 
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

import reddit_bot.domain.entity
import java.util.Date
import java.time.LocalDateTime
import java.time.Month
import scala.runtime.ScalaRunTime
import org.slf4j.LoggerFactory

class FeedSubredditPersistence(transactor: Transactor[IO]){
    def getFeedSubreddit(subredditId: Long, feedId: Long): IO[FeedSubreddit] = {
        sql"select feed_id,subreddit_id,flair from feed_subreddit where feed_id=$feedId and subreddit_id=$subredditId"
            .query[FeedSubreddit]
            .unique
            .transact(transactor)

    }
}

case class FeedSubreddit(feedId:Long, subredditId: Long, flair: String){
    override def toString = ScalaRunTime._toString(this)

}