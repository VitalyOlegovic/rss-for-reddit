package reddit_bot.domain.dto;

import reddit_bot.domain.entity.Subreddit;

public class SubredditDTO {

    private long id;
    private String name;
    private Integer dailyQuota;
    private Integer priority;
    private Boolean enabled;
    private Integer recentFeedsWindow;
    private Boolean moderator;

    public SubredditDTO(Subreddit subreddit){
        id = subreddit.getId();
        name = subreddit.getName();
        dailyQuota = subreddit.getDailyQuota();
        priority = subreddit.getPriority();
        enabled = subreddit.getEnabled();
        recentFeedsWindow = subreddit.getRecentFeedsWindow();
        moderator = subreddit.getModerator();
    }

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

    public Integer getDailyQuota() {
        return dailyQuota;
    }

    public void setDailyQuota(Integer dailyQuota) {
        this.dailyQuota = dailyQuota;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRecentFeedsWindow() {
        return recentFeedsWindow;
    }

    public void setRecentFeedsWindow(Integer recentFeedsWindow) {
        this.recentFeedsWindow = recentFeedsWindow;
    }

    public Boolean getModerator() {
        return moderator;
    }

    public void setModerator(Boolean moderator) {
        this.moderator = moderator;
    }
}
