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
import cats.free.Free
import doobie.free.connection

class LinkSendingPersistence(transactor: Transactor[IO]){
    def save(linkSending: LinkSending): IO[Int] = {
        for{
            id <- sql"select max(id) +1 from link_sending".query[Option[Long]].unique.transact(transactor)
            result <- sql"insert into link_sending values ($id,${linkSending.linkId},${linkSending.sendingDate},${linkSending.subredditId})"
                .update.run.transact(transactor)
        }yield result
    }

    def countLinksSentAfter(subredditId: Long, date: Date): IO[Option[Int]] = {
        sql"select count(1) from link_sending where subreddit_id = $subredditId and sending_date >= $date"
            .query[Option[Int]]
            .unique
            .transact(transactor)

    }

    def linksSentAfter(subredditId: Long, date: Date): IO[Option[Int]] = {
        sql"select distinct(id) from link_sending where subreddit_id = $subredditId and sending_date >= $date"
            .query[Option[Int]]
            .unique
            .transact(transactor)

    }

    def feedsSentAfter(subredditId: Long, date: Date): IO[List[Int]] = {
        sql"select distinct(l.feed_id) from link_sending ls join link l on ls.link_id = l.id where subreddit_id = $subredditId and sending_date >= $date"
            .query[Int]
            .to[List]
            .transact(transactor)
    }
}

case class LinkSending(id: Long, linkId: Long, sendingDate: LocalDateTime, subredditId: Long){
    def this(linkId: Long, sendingDate: LocalDateTime, subredditId: Long) = this(-1,linkId,sendingDate,subredditId)
    override def toString = ScalaRunTime._toString(this)
}