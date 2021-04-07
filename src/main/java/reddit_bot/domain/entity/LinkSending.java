package reddit_bot.domain.entity;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="link_sending")
public class LinkSending  implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Integer id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="link_id")
    private Link link;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="subreddit_id")
    private Subreddit subreddit;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="sending_date", nullable=false)
    private Date sendingDate;

    public LinkSending() {
    }


    public LinkSending(Date sendingDate) {
        this.sendingDate = sendingDate;
    }
    public LinkSending(Link link, Subreddit subreddit, Date sendingDate) {
        this.link = link;
        this.subreddit = subreddit;
        this.sendingDate = sendingDate;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Link getLink() {
        return this.link;
    }

    public void setLink(Link link) {
        this.link = link;
    }



    public Subreddit getSubreddit() {
        return this.subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }


    public Date getSendingDate() {
        return this.sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }


}


