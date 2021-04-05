package reddit_bot.persistence

import doobie._
import doobie.implicits._
import cats.effect.{ContextShift, IO}

import scala.concurrent.ExecutionContext
import scala.runtime.ScalaRunTime

class SubredditPersistence {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  def read(): Option[Subreddit] = {


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

  def find(): ConnectionIO[Option[Subreddit]] =
    sql"select id, name from subreddits limit 1".query[Subreddit].option

}
