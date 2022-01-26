package main.java.org.ce.ap.server;

public interface TweetingService {
    /**
     * this method handles the works that should be done in tweeting service
     */
    void act();

    /**
     * this method adds a tweet to user's tweets
     */
    void add() throws InterruptedException;

    /**
     * this method removes a tweet or retweet from user's tweets or retweets
     */
    void remove() throws InterruptedException;

    /**
     * * this method likes a tweet from a user's tweets
     */
    void like() throws InterruptedException;
}
