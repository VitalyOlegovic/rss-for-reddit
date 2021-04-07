package reddit_bot.domain.bean;

public class SubredditBean {

    private long id;
    private String name;
    private long dailyQuota;
    private long priority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDailyQuota() {
        return dailyQuota;
    }

    public void setDailyQuota(long dailyQuota) {
        this.dailyQuota = dailyQuota;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "reddit_bot.domain.bean.SubredditBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dailyQuota=" + dailyQuota +
                ", priority=" + priority +
                '}';
    }
}
