package reddit_bot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.service.LinkService;

@Controller
public class TestController implements ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    private static final String PATH = "/error";

    @Autowired
    LinkService linkService;

    @Autowired
    RedditSubmitterService redditSubmitterService;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "ciao";
    }

    @GetMapping("/hello")
    String hello() {
        return "hello";
    }

    /*
    @RequestMapping(value = PATH)
    @ResponseBody
    public String error(HttpServletRequest aRequest) {
        logger.error(aRequest.toString());
        return aRequest.toString();
    }*/

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping("/updateFeeds")
    @ResponseBody
    public void updateFeeds(){
        linkService.updateFeeds();
    }

    @RequestMapping("/send")
    @ResponseBody
    public void send(){
        redditSubmitterService.send();
    }

}
