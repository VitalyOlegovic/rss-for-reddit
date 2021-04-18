package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._

import scala.concurrent.ExecutionContext
import scala.runtime.ScalaRunTime

class SubredditPersistence {
  
  def read(): IO[List[Subreddit]] = {
    find().transact(Database.transactor)
  }

  case class Subreddit(id: Long, name: String){
    override def toString = ScalaRunTime._toString(this)
  }

  def find(): ConnectionIO[List[Subreddit]] =
    sql"select id, name from subreddits where enabled = true order by priority"
      .query[Subreddit]
      .to[List]

}
