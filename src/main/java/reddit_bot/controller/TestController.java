package reddit_bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reddit_bot.reddit.RedditSubmitterService;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;
import reddit_bot.service.LinkService;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "ciao";
    }

    @RequestMapping(value = PATH)
    @ResponseBody
    public String error(HttpServletRequest aRequest) {
        return aRequest.toString();
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
