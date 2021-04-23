package reddit_bot.infrastructure.repository

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

import reddit_bot.domain.entity
import java.util.Date
import scala.runtime.ScalaRunTime
import org.slf4j.LoggerFactory

class LinkPersistence(transactor: Transactor[IO]){
    private val logger = LoggerFactory.getLogger(classOf[LinkPersistence])
    //implicit val han = LogHandler.jdkLogHandler

    case class Link(id: Long, title: String, url: String, publication_date: Date, feed_id: Long){
        override def toString = ScalaRunTime._toString(this)

        def toEntity(): entity.Link = {
            new entity.Link(id,title,url,publication_date)
        } 
    }

    def findByUrl(url: String): IO[List[Link]] = {
        sql"select id,title,url,publication_date,feed_id from links where url=$url"
            .query[Link]
            .to[List]
            .transact(transactor)
    }

    def countByUrl(url: String): IO[Option[Int]] = {
        sql"select count(*) from links where url=$url"
            .query[Int]
            .option
            .transact(transactor)
    }

    def insert(link: entity.Link, foundLinksCount: Option[Int], maxId: Int): IO[Int] = {
        logger.info("Link: " + link.toString() + " links count: " + foundLinksCount)
        if(foundLinksCount == Some(0)){
            logger.info("Inserting link: " + link.toString())
            sql"insert into links (id,title,url,publication_date,feed_id) values (${maxId + 1},${link.getTitle},${link.getUrl},${link.getPublicationDate},${link.getFeed.getId})"
                .update
                //.withUniqueGeneratedKeys[Int]("id")
                .run
                .transact(transactor)
        }else{
            IO.pure(0)
        }
    }

    def findByFeedIds(notSentFeeds: List[Long]) = {
        val idList = notSentFeeds.mkString(",")
        sql"select id,title,url,publication_date,feed_id from links where feed_id in ($idList)"
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