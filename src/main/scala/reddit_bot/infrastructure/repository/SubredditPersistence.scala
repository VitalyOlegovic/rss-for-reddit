package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._

import scala.concurrent.ExecutionContext
import scala.runtime.ScalaRunTime
import reddit_bot.domain.entity
import java.util.HashSet

class SubredditPersistence(transactor: Transactor[IO]) {
  
  def findEnabled(): IO[List[Subreddit]] = {
    sql"select id, name, daily_quota, priority, enabled from subreddits where enabled = true order by priority"
      .query[Subreddit]
      .to[List]
      .transact(transactor)
  }



  def findAll(): IO[List[Subreddit]] =
    sql"select id, name, daily_quota, priority, enabled from subreddits"
      .query[Subreddit]
      .to[List]
      .transact(transactor)
    

}

case class Subreddit(
  id: Long, 
  name: String, 
  dailyQuota: Int, 
  priority: Int, 
  enabled: Boolean,
  recentFeedsWindow: Option[Int],
  moderator: Boolean
  ){
  override def toString = ScalaRunTime._toString(this)

  def toEntity() : entity.Subreddit = 
    new entity.Subreddit(
      id,name,dailyQuota,priority,enabled, new HashSet[entity.FeedSubreddit]()
    )
  
}