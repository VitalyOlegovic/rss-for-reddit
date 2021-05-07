package reddit_bot_test.infrastructure.repository

import reddit_bot.infrastructure.repository.LinkSendingPersistence
import reddit_bot.infrastructure.repository.Database
import reddit_bot.infrastructure.repository.LinkSending
import java.time.LocalDateTime
import org.junit.Test
import org.junit.Before

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._

class LinkSendingPersistenceTest{

    val transactor = TestDatabase.transactor()

    @Before def initialize() {
        sql"drop table link_sending".update.run.transact(transactor).unsafeRunSync()

        val connection = sql"""CREATE TABLE link_sending (
            id int NOT NULL AUTO_INCREMENT,
            link_id int not NULL,
            sending_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            subreddit_id int NOT NULL,
            PRIMARY KEY (`id`)
        ) """.update.run
        connection.transact(transactor).unsafeRunSync()
    }

    @Test def test: Unit = {
        val lsp = new LinkSendingPersistence(transactor)
        val ls = new LinkSending(-1, LocalDateTime.now(), 1)
        lsp.save(ls).unsafeRunSync()
    }

}