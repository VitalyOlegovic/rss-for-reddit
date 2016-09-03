package reddit_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;
import reddit_bot.service.LinkService;

import javax.persistence.EntityManager;

@Controller
public class TestController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    LinkService linkService;

    @Autowired
    RedditSubmitterService redditSubmitterService;

    @Autowired
    LinkSendingRepository linkSendingRepository;

    @Autowired
    SubredditRepository subredditRepository;

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

        redditSubmitterService.send();

        /*Link link = linkRepository.findOne(98304L);
        Subreddit subreddit = subredditRepository.findOne(1L);

        LinkSending linkSending = new LinkSending(link,subreddit, new Date());
        linkSendingRepository.save(linkSending);*/

        return "ciao";
    }

    @RequestMapping("/rss")
    String rss(){
        linkService.updateFeeds();
        return "ok";
    }

}
