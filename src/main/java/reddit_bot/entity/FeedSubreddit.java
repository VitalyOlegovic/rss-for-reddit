package reddit_bot.entity;

import javax.persistence.*;

@Entity
@Table(name="feed_subreddit")
public class FeedSubreddit  implements java.io.Serializable {

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name="feedId", column=@Column(name="feed_id", nullable=false) ),
            @AttributeOverride(name="subredditId", column=@Column(name="subreddit_id", nullable=false) ) } )
    private FeedSubredditId id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="feed_id", nullable=false, insertable=false, updatable=false)
    private Feed feed;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="subreddit_id", nullable=false, insertable=false, updatable=false)
    private Subreddit subreddit;

    @Column(name="flair")
    private String flair;

    public FeedSubreddit() {
    }

    public FeedSubreddit(FeedSubredditId id, Feed feed, Subreddit subreddit) {
        this.id = id;
        this.feed = feed;
        this.subreddit = subreddit;
    }


    public FeedSubredditId getId() {
        return this.id;
    }

    public void setId(FeedSubredditId id) {
        this.id = id;
    }


    public Feed getFeed() {
        return this.feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }


    public Subreddit getSubreddit() {
        return this.subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }


    public String getFlair() {
        return flair;
    }

    public void setFlair(String flair) {
        this.flair = flair;
    }
}


