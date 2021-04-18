package reddit_bot.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object Database{
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    def transactor() = Transactor.fromDriverManager[IO](
      "com.mysql.cj.jdbc.Driver",
      "jdbc:mysql://database:3306/redditbot",
      "redditbot",
      "redditbot"
    )
}