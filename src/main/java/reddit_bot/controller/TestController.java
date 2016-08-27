package reddit_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reddit_bot.rssfeeds.RSSService;

import javax.persistence.EntityManager;

@RestController
public class TestController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RSSService rssService;

    @RequestMapping("/")
    String home() {
        rssService.updateFeeds();
        return "Ciao";
    }

}
