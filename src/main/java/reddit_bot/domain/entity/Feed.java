package reddit_bot.domain.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="feeds")
public class Feed implements java.io.Serializable {

    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy=GenerationType.TABLE)
    private long id;
    @Column(name="url", nullable=false)
    private String url;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="feed")
    private Set<Link> links = new HashSet<Link>(0);
    @OneToMany(fetch=FetchType.EAGER, mappedBy="feed")
    private Set<FeedSubreddit> feedSubreddits = new HashSet<FeedSubreddit>(0);

    public Feed() {
    }


    public Feed(long id, String url) {
        this.id = id;
        this.url = url;
    }
    public Feed(long id, String url, Integer parentFeed, Set<Link> links, Set<FeedSubreddit> feedSubreddits) {
        this.id = id;
        this.url = url;
        this.links = links;
        this.feedSubreddits = feedSubreddits;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Link> getLinks() {
        return this.links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }


    public Set<FeedSubreddit> getFeedSubreddits() {
        return this.feedSubreddits;
    }

    public void setFeedSubreddits(Set<FeedSubreddit> feedSubreddits) {
        this.feedSubreddits = feedSubreddits;
    }


    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}


