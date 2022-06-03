package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object Database {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  def mySqlTransactor() = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    "jdbc:mysql://database:3306/redditbot",
    "redditbot",
    "redditbot"
  )

  def h2Transactor() = Transactor.fromDriverManager[IO](
    "org.h2.Driver",
    "jdbc:h2:~/test",
    "sa",
    ""
  )

  def transactor = h2Transactor()
}