package reddit_bot.infrastructure.repository

import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.implicits.javatimedrivernative._
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

class LinkPersistence(transactor: Transactor[IO]){
    private val logger = LoggerFactory.getLogger(classOf[LinkPersistence])
    //implicit val han = LogHandler.jdkLogHandler



    def findByUrl(url: String): IO[List[Link]] = {
        sql"select id,title,url,publication_date,feed_id from links where url=$url"
            .query[Link]
            .to[List]
            .transact(transactor)
    }

    def countByUrl(url: String): IO[Int] = {
        sql"select count(*) from links where url=$url"
            .query[Int]
            .unique
            .transact(transactor)
    }

    def insert(link: entity.Link, maxId: Long): IO[Int] =  {
            logger.info("Inserting link: " + link.toString())
            val publication_date = Option(link.getPublicationDate).getOrElse(LocalDateTime.of(1970, Month.JANUARY,1,0,0))
            sql"""insert into links (id,title,url,publication_date,feed_id) 
            values (${maxId + 1},${link.getTitle},${link.getUrl},${publication_date},${link.getFeed.getId})"""
                .update
                //.withUniqueGeneratedKeys[Int]("id")
                .run
                .transact(transactor)

        }
    

    def findNotSentByFeedIds(notSentFeeds: List[Long]) = {
        val idList = notSentFeeds.mkString(",")
        sql"""select id,title,url,publication_date,feed_id 
        from links l where feed_id in ($idList) 
        order by l.publication_date desc"""
            .query[Link]
            .to[List]
            .transact(transactor)
    }

    def maxId(): IO[Int] = {
        sql"select max(id) from links"
            .query[Int]
            .unique
            .transact(transactor)
    }
}

case class Link(id: Long, title: String, url: String, publication_date: LocalDateTime, feed_id: Long){
    override def toString = ScalaRunTime._toString(this)

    def toEntity(transactor: Transactor[IO]): IO[entity.Link] = {
        for{
            feed <- new FeedPersistence(transactor).findOne(feed_id).map(_.toEntity())
            link = new entity.Link(id,title,url,publication_date, feed)
        }yield link
    } 
}