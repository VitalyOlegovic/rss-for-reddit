package reddit_bot.entity;
// Generated 27-ago-2016 1.54.44 by Hibernate Tools 4.3.2-SNAPSHOT


import javax.persistence.*;

@Entity
@Table(name="feed_subreddit")
public class FeedSubreddit  implements java.io.Serializable {


    private FeedSubredditId id;
    private Feed feed;
    private Subreddit subreddit;

    public FeedSubreddit() {
    }

    public FeedSubreddit(FeedSubredditId id, Feed feed, Subreddit subreddit) {
        this.id = id;
        this.feed = feed;
        this.subreddit = subreddit;
    }

    @EmbeddedId


    @AttributeOverrides( {
            @AttributeOverride(name="feedId", column=@Column(name="feed_id", nullable=false) ),
            @AttributeOverride(name="subredditId", column=@Column(name="subreddit_id", nullable=false) ) } )
    public FeedSubredditId getId() {
        return this.id;
    }

    public void setId(FeedSubredditId id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="feed_id", nullable=false, insertable=false, updatable=false)
    public Feed getFeed() {
        return this.feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="subreddit_id", nullable=false, insertable=false, updatable=false)
    public Subreddit getSubreddit() {
        return this.subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }




}


