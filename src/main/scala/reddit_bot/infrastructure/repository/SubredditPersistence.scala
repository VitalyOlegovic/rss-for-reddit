package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._

import scala.concurrent.ExecutionContext
import scala.runtime.ScalaRunTime
import reddit_bot.domain.entity.FeedSubreddit
import java.util.HashSet

object SubredditPersistence {
  
  def findEnabled(): IO[List[Subreddit]] = {
    find().transact(Database.transactor)
  }

  case class Subreddit(
    id: Long, 
    name: String, 
    dailyQuota: Int, 
    priority: Int, 
    enabled: Boolean
    ){
    override def toString = ScalaRunTime._toString(this)

    def toEntity() : reddit_bot.domain.entity.Subreddit = 
      new reddit_bot.domain.entity.Subreddit(
        id,name,dailyQuota,priority,enabled, new HashSet[FeedSubreddit]()
      )
    
  }

  def find(): ConnectionIO[List[Subreddit]] =
    sql"select id, name, dailyQuota, priority, enabled from subreddits where enabled = true order by priority"
      .query[Subreddit]
      .to[List]

}
