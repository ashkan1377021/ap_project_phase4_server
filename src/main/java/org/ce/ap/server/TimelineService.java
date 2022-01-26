package main.java.org.ce.ap.server;

public interface TimelineService {
    /**
     * this method shows Tweets,reTweets and likes of each favorite user to it and also tweets of itself
     */
    void showTweets() throws InterruptedException;
}
