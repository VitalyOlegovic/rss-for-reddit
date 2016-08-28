package reddit_bot.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="subreddits")
public class Subreddit implements java.io.Serializable {

    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy=GenerationType.TABLE)
    private long id;
    @Column(name="name", length=256)
    private String name;
    @Column(name="daily_quota")
    private Integer dailyQuota;
    @Column(name="priority")
    private Integer priority;
    @Column(name="enabled")
    private Boolean enabled;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="subreddit")
    private Set<FeedSubreddit> feedSubreddits = new HashSet<FeedSubreddit>(0);

    public Subreddit() {
    }


    public Subreddit(long id) {
        this.id = id;
    }
    public Subreddit(long id, String name, Integer dailyQuota, Integer priority, Boolean enabled, Set<FeedSubreddit> feedSubreddits) {
        this.id = id;
        this.name = name;
        this.dailyQuota = dailyQuota;
        this.priority = priority;
        this.enabled = enabled;
        this.feedSubreddits = feedSubreddits;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Integer getDailyQuota() {
        return this.dailyQuota;
    }

    public void setDailyQuota(Integer dailyQuota) {
        this.dailyQuota = dailyQuota;
    }



    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }



    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public Set<FeedSubreddit> getFeedSubreddits() {
        return this.feedSubreddits;
    }

    public void setFeedSubreddits(Set<FeedSubreddit> feedSubreddits) {
        this.feedSubreddits = feedSubreddits;
    }

    @Override
    public String toString() {
        return "Subreddit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dailyQuota=" + dailyQuota +
                ", priority=" + priority +
                ", enabled=" + enabled +
                ", feedSubreddits=" + feedSubreddits +
                '}';
    }
}


