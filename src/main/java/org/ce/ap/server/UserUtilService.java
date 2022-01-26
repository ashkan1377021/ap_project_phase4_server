package main.java.org.ce.ap.server;

import java.io.IOException;

public interface UserUtilService {
    /**
     * this method handles the works that should be done in UserUtil service
     */
    void act();
    /**
     * this method sets a picture for the user
     */
     void setImage() throws IOException;
    /**
     * this method send information of a user to the user
     */
     void sendInformation() throws IOException, InterruptedException;
    /**
     * this method checks that a username exists or not
     */
    void checkUsernameExistence();
}
