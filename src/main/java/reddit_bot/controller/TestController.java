package reddit_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reddit_bot.entity.Link;
import reddit_bot.reddit.RedditService;
import reddit_bot.repository.LinkRepository;
import reddit_bot.rssfeeds.RSSService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    RSSService rssService;

    @Autowired
    RedditService redditService;

    @RequestMapping("/")
    String home() {
        /*List<Integer> longList = new ArrayList<Integer>();
        longList.add(1);
        longList.add(2);
        longList.add(5);
        longList.add(8);
        longList.add(9);
        Iterable<Link> links = linkRepository.findByFeedIds(longList, "it");
        return links.toString();*/

        redditService.send();

        return "ciao";
    }

    @RequestMapping("/rss")
    String rss(){
        rssService.updateFeeds();
        return "ok";
    }

}
