package reddit_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reddit_bot.bean.LinkBean;
import reddit_bot.entity.Link;
import reddit_bot.entity.LinkSending;
import reddit_bot.entity.Subreddit;
import reddit_bot.jdbc.LinkDAO;
import reddit_bot.repository.LinkRepository;
import reddit_bot.repository.LinkSendingRepository;
import reddit_bot.repository.SubredditRepository;

import java.util.GregorianCalendar;
import java.util.List;

@Service
public class MigrationService {

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    LinkSendingRepository linkSendingRepository;


    public void migrate(){
        Iterable<Subreddit> subredditIterable = subredditRepository.findAll();
        LinkDAO linkDAO = new LinkDAO();

        for(Subreddit subreddit : subredditIterable){
            List<LinkBean> linkBeanList = linkDAO.published(subreddit.getName());
            for(LinkBean linkBean : linkBeanList){
                Link link = linkRepository.findByUrl(linkBean.getUrl().toExternalForm()).iterator().next();
                LinkSending linkSending = new LinkSending(link, subreddit, new GregorianCalendar(2016, 8, 20).getTime());
                linkSendingRepository.save(linkSending);
            }
        }
    }

}
