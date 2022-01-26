package main.java.org.ce.ap.server;

public interface ObserverService {
    /**
     * this method handles the works that should be done in observer service
     */
    void act();

    /**
     * this method handles process of following a user
     */
    void follow() throws InterruptedException;

    /**
     * this method handles process of unfollowing a user
     */
    void unfollow() throws InterruptedException;

}
