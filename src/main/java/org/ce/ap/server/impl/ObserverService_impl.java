package main.java.org.ce.ap.server.impl;

import main.java.org.ce.ap.server.File_utility;
import main.java.org.ce.ap.server.ObserverService;
import main.java.org.ce.ap.server.User;
import main.java.org.ce.ap.server.usefulMethods;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class has Methods that a user can follow and unfollow another user
 *
 * @author ashkan_mogharab
 * @version version 1 of  ObserverService implementation
 */
public class ObserverService_impl implements ObserverService {
    //a socket for connecting  to user
    private final Socket connectionSocket;
    //an object of usefulMethods
    private final usefulMethods usefulmethods = new usefulMethods();
    //users of ServerSide
    private final ArrayList<User> users;
    //index of the user who wants to use from tweeting service
    private final int index;
    // an object of File_utility
    File_utility file_utility = new File_utility();
    // an input stream
    private InputStream in;
    // select of the person
    private String select;

    /**
     * creates a new observer service
     *
     * @param users            users of ServerSide
     * @param index            index of the user who wants to use observer service
     * @param connectionSocket a socket for connecting  to user
     */
    public ObserverService_impl(ArrayList<User> users, int index, Socket connectionSocket) {
        this.users = users;
        this.index = index;
        this.connectionSocket = connectionSocket;
        act();
    }

    /**
     * this method handles the works that should be done in observer service
     */
    public void act() {
        try {
            in = connectionSocket.getInputStream();
            String msg;
            System.out.println(users.get(index).getUsername() + " came to Observer service");
            file_utility.record_events(users.get(index).getUsername() + " came to Observer service");
            System.out.println("receive from " + users.get(index).getUsername() + " : " + (msg = usefulmethods.read_message(in)));
            file_utility.record_events("receive from " + users.get(index).getUsername() + " : " + msg);
            switch (msg) {
                case "1":
                    follow();
                    break;
                case "2":
                    unfollow();
                    break;
            }
            connectionSocket.close();
            in.close();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * this method handles process of following a user
     */
    public void follow() throws InterruptedException {
        System.out.println("process of follow a user by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of follow a user by " + users.get(index).getUsername() + "  started");
        String Username = (usefulmethods.read_message(in));
        System.out.println(users.get(index).getUsername() + " wants to follow " + Username);
        file_utility.record_events(users.get(index).getUsername() + " wants to follow " + Username);
        int ix = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(Username)) {
                ix = i;
                break;
            }
        }
        users.get(ix).getFollowers().add(users.get(index));
        users.get(index).getFavoriteUsers().add(users.get(ix));
        file_utility.make_changes(users);
        System.out.println("follow process of " + Username + " by " + users.get(index).getUsername() + " successfully done");
        file_utility.record_events("follow process of " + Username + " by " + users.get(index).getUsername() + " successfully done");
        Thread.sleep(1);
    }

    /**
     * this method handles process of unfollowing a user
     */
    public void unfollow() throws InterruptedException {
        System.out.println("process of unfollow a user by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of unfollow a user by " + users.get(index).getUsername() + "  started");
        String Username = (usefulmethods.read_message(in));
        System.out.println(users.get(index).getUsername() + " wants to unfollow " + Username);
        file_utility.record_events(users.get(index).getUsername() + " wants to unfollow " + Username);
        int ix = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(Username)) {
                ix = i;
                break;
            }
        }
        users.get(ix).getFollowers().remove(users.get(index));
        users.get(index).getFavoriteUsers().remove(users.get(ix));
        file_utility.make_changes(users);
        System.out.println("unfollow process of " + Username + " by " + users.get(index).getUsername() + " successfully done");
        file_utility.record_events("unfollow process of " + Username + " by " + users.get(index).getUsername() + " successfully done");
        Thread.sleep(1);
    }
}
