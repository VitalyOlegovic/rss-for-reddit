package reddit_bot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController implements ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    private static final String PATH = "/error";

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
}
