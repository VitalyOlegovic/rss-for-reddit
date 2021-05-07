package reddit_bot_test.infrastructure.repository

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object TestDatabase{
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    def transactor() = Transactor.fromDriverManager[IO](
      "org.h2.Driver",
      "jdbc:h2:~/test",
      "sa",
      ""
    )
}