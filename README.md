# Java RSS to Reddit

A project to read XML feeds and publish them on reddit.

It does not flood, because you can configure the number of links to post daily for every subreddit.

It posts links from each feed at most once per subreddit.

It has a Spring Boot scheduler. To use crontab, see: https://www.raspberrypi.org/documentation/linux/usage/cron.md

## Similar projects:

https://github.com/Ferocit/RedditRssBot

https://github.com/eyalgo/rss-reader

## Run with Docker

    docker build -t reddit-bot/reddit-bot .

## Database fixes
    alter table feed_subreddit change feed_id feed_id bigint;
    alter table feed_subreddit change subreddit_id subreddit_id bigint;