package main.java.org.ce.ap.server;

public interface AuthenticationService {
    /**
     * this method handles the works that should be done in authentication service
     */
    void act();

    /**
     * this method does sign up process
     */
    void signUp();

    /**
     * this method does sign in process
     */
    void signIn();
}
