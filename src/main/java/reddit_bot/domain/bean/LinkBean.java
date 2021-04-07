package reddit_bot.domain.bean;

import java.net.URL;
import java.util.Date;

public class LinkBean {

    private Long id;
    private String title;
    private URL url;
    private Date publicationDate;
    private Boolean sent;
    private String subreddit;
    private Long feedId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    @Override
    public String toString() {
        return "reddit_bot.domain.bean.LinkBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url=" + url +
                ", publicationDate=" + publicationDate +
                ", sent=" + sent +
                ", subreddit='" + subreddit + '\'' +
                ", feedId=" + feedId +
                '}';
    }
}
