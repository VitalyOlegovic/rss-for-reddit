package reddit_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reddit_bot.dto.SubredditDTO;
import reddit_bot.service.SubredditService;

@RestController
public class SubredditController {

    @Autowired
    SubredditService subredditService;

    @RequestMapping("/subreddit")
    @ResponseBody
    public Iterable<SubredditDTO> subreddits(){
        return subredditService.subreddits();
    }

}
