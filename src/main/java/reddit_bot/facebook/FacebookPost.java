package reddit_bot.facebook;

import java.net.URL;

public class FacebookPost {

    private URL url;
    private String name;
    private String description;
    private String message;

    public FacebookPost(){}

    public FacebookPost(URL url, String name, String description, String message) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.message = message;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
