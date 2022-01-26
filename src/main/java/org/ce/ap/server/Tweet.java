package main.java.org.ce.ap.server;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * this class holds details of a tweet
 *
 * @author ashkan_mogharab
 */
public class Tweet implements Serializable {
    //sender of the tweet
    private final User sender;
    //users who liked  the tweet
    private final ArrayList<User> likes;
    // users who retweeted the tweet
    private final ArrayList<User> retweets;
    //text of the tweet that its maximum length is 256
    private final String text;
    // send time of the tweet
    private final LocalDateTime sendTime;

    /**
     * creates a new tweet
     *
     * @param sender   sender of the tweet
     * @param text     text of the tweet
     * @param sendTime send date of the tweet
     */
    public Tweet(User sender, String text, LocalDateTime sendTime) {
        this.sender = sender;
        this.likes = new ArrayList<>();
        this.retweets = new ArrayList<>();
        this.text = text;
        this.sendTime = sendTime;
    }

    /**
     * getter
     *
     * @return sender of the tweet
     */
    public User getSender() {
        return sender;
    }

    /**
     * getter
     *
     * @return users who liked  the tweet
     */
    public ArrayList<User> getLikes() {
        return likes;
    }

    /**
     * getter
     *
     * @return usernames of users who retweeted the tweet
     */
    public ArrayList<User> getRetweets() {
        return retweets;
    }

    /**
     * getter
     *
     * @return text of the tweet
     */
    public String getText() {
        return text;
    }

    /**
     * getter
     *
     * @return send time of the tweet
     */
    public LocalDateTime getSendDate() {
        return sendTime;
    }
}
