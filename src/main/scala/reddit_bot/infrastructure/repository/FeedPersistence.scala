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

class FeedPersistence(transactor: Transactor[IO]){

    case class Feed(id: Long, url: String){
        override def toString = ScalaRunTime._toString(this)

        def toEntity() : entity.Feed = new entity.Feed(id,url)
    }

    def findBySubredditId(subredditId: Long): IO[List[Feed]] = {
        sql"""select id, url from feeds f join feed_subreddit fs on fs.feed_id = f.id
          where fs.subreddit_id=$subredditId"""
          .query[Feed]
          .to[List]
          .transact(transactor)
    }

    def findOne(feedId: Long): IO[Feed] = {
        sql"select id,url from feeds where id=$feedId"
            .query[Feed]
            .unique
            .transact(transactor)
    }
}