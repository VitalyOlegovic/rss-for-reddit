package reddit_bot.entity;
// Generated 27-ago-2016 1.54.44 by Hibernate Tools 4.3.2-SNAPSHOT


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="subreddits")
public class Subreddit implements java.io.Serializable {

    private int id;
    private String name;
    private Integer dailyQuota;
    private Integer priority;
    private Boolean enabled;
    private Set<FeedSubreddit> feedSubreddits = new HashSet<FeedSubreddit>(0);

    public Subreddit() {
    }


    public Subreddit(int id) {
        this.id = id;
    }
    public Subreddit(int id, String name, Integer dailyQuota, Integer priority, Boolean enabled, Set<FeedSubreddit> feedSubreddits) {
        this.id = id;
        this.name = name;
        this.dailyQuota = dailyQuota;
        this.priority = priority;
        this.enabled = enabled;
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


    @Column(name="name", length=256)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name="daily_quota")
    public Integer getDailyQuota() {
        return this.dailyQuota;
    }

    public void setDailyQuota(Integer dailyQuota) {
        this.dailyQuota = dailyQuota;
    }


    @Column(name="priority")
    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


    @Column(name="enabled")
    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="subreddits")
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


