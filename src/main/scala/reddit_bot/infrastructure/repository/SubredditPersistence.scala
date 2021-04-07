package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._

import scala.concurrent.ExecutionContext
import scala.runtime.ScalaRunTime

class SubredditPersistence {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  def read(): List[Subreddit] = {


    val xa = Transactor.fromDriverManager[IO](
      "com.mysql.cj.jdbc.Driver",
      "jdbc:mysql://database:3306/redditbot",
      "redditbot",
      "redditbot"
    )

    find().transact(xa).unsafeRunSync()
  }

  case class Subreddit(id: Long, name: String){
    override def toString = ScalaRunTime._toString(this)
  }

  def find(): ConnectionIO[List[Subreddit]] =
    sql"select id, name from subreddits where enabled = true order by priority"
      .query[Subreddit]
      .to[List]

}
