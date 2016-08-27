package reddit_bot.entity;
// Generated 27-ago-2016 1.54.44 by Hibernate Tools 4.3.2-SNAPSHOT


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="feeds")
public class Feed implements java.io.Serializable {


    private int id;
    private String url;
    private Integer parentFeed;
    private Set<Link> linkses = new HashSet<Link>(0);
    private Set<FeedSubreddit> feedSubreddits = new HashSet<FeedSubreddit>(0);

    public Feed() {
    }


    public Feed(int id, String url) {
        this.id = id;
        this.url = url;
    }
    public Feed(int id, String url, Integer parentFeed, Set<Link> linkses, Set<FeedSubreddit> feedSubreddits) {
        this.id = id;
        this.url = url;
        this.parentFeed = parentFeed;
        this.linkses = linkses;
        this.feedSubreddits = feedSubreddits;
    }

    @Id


    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name="url", nullable=false, length=256)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Column(name="parent_feed")
    public Integer getParentFeed() {
        return this.parentFeed;
    }

    public void setParentFeed(Integer parentFeed) {
        this.parentFeed = parentFeed;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="feeds")
    public Set<Link> getLinkses() {
        return this.linkses;
    }

    public void setLinkses(Set<Link> linkses) {
        this.linkses = linkses;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="feeds")
    public Set<FeedSubreddit> getFeedSubreddits() {
        return this.feedSubreddits;
    }

    public void setFeedSubreddits(Set<FeedSubreddit> feedSubreddits) {
        this.feedSubreddits = feedSubreddits;
    }




}


