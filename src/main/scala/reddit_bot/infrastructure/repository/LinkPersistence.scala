package reddit_bot.infrastructure.repository

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

import reddit_bot.domain.entity.Link

class LinkPersistence(transactor: Transactor[IO]){
    def insert(link: Link) : IO[Int] = {
        sql"insert into links (title,url,publication_date,feed_id) values (${link.getTitle},${link.getUrl},${link.getPublicationDate},${link.getFeed.getId})"
            .update
            .run
            .transact(transactor)
    }
}