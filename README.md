# RSS to Reddit

A project to read XML feeds and publish them on reddit.

It does not flood, because you can configure the number of links to post daily for every subreddit.

It posts links from each feed at most once per subreddit.

It has a Spring Boot scheduler. To use crontab, see: https://www.raspberrypi.org/documentation/linux/usage/cron.md

## Similar projects:

https://github.com/Ferocit/RedditRssBot

https://github.com/eyalgo/rss-reader

## Run with Docker

    docker build -t reddit-bot/reddit-bot .

## Roadmap

The roadmap for the project is the following:

* debug the Java / Maven / Spring Boot / Java Persistence API version
* replace Java with Scala where it makes sense
* replace Java Persistence API with Doobie and Quill
* replace Spring with FS2
* replace Maven with SBT
* find alternatives for JRAW and ROME

## Lessons learned

There are some things you can't do

* can't use Spring Boot with sbt
* can't update Spring Boot version, because it creates conflicts with JRAW