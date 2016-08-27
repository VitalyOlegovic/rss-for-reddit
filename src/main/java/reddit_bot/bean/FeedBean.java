package reddit_bot.bean;

public class FeedBean {
    private long id;
    private String url;
    private String subreddit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    @Override
    public String toString() {
        return "reddit_bot.bean.FeedBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", subreddit='" + subreddit + '\'' +
                '}';
    }
}
