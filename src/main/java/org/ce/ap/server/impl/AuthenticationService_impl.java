package main.java.org.ce.ap.server.impl;

import main.java.org.ce.ap.server.AuthenticationService;
import main.java.org.ce.ap.server.File_utility;
import main.java.org.ce.ap.server.User;
import main.java.org.ce.ap.server.usefulMethods;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * this class can sign up persons in tweeter and also can allow to previous users to sign in tweeter
 *
 * @author ashkan_mogharab
 * @version version 1 of AuthenticationService implementation
 */

public class AuthenticationService_impl implements AuthenticationService {
    //a socket for connecting  to user
    private final Socket connectionSocket;
    //client number
    private final int clientNum;
    //an object of usefulMethods
    private final usefulMethods usefulmethods = new usefulMethods();
    //users of ServerSide
    private final ArrayList<User> users;
    // an object of File_utility
    File_utility file_utility = new File_utility();
    // an input stream
    private InputStream in;
    // an output stream
    private OutputStream out;
    //index of user who signs in or signs up
    private int j = -1;

    /**
     * creates a new  authentication service
     *
     * @param users            users of ServerSide
     * @param clientNum        number of client
     * @param connectionSocket a socket for connecting  to user
     */
    public AuthenticationService_impl(ArrayList<User> users, int clientNum, Socket connectionSocket) throws IOException {
        this.users = users;
        this.clientNum = clientNum;
        this.connectionSocket = connectionSocket;
        act();
    }

    /**
     * this method handles the works that should be done in authentication service
     */
    public void act() {
        try {
            out = connectionSocket.getOutputStream();
            in = connectionSocket.getInputStream();
            String msg = usefulmethods.read_message(in);
            if (msg.equals("1"))
                signUp();
            else if (msg.equals("2"))
                signIn();
            connectionSocket.close();
            out.close();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method does sign up process
     */
    public void signUp() {
        boolean sign1 = true;
        boolean sign2 = true;
        String firstname;
        String lastname;
        String username;
        String password;
        LocalDate birthDate = null;
        String bio;
        System.out.println("process of client" + clientNum + " sign up started");
        file_utility.record_events("process of client" + clientNum + " sign up started");
        firstname = usefulmethods.read_message(in);
        System.out.println("first name of client" + clientNum + " is:" + firstname);
        file_utility.record_events("first name of client" + clientNum + " is:" + firstname);
        lastname = usefulmethods.read_message(in);
        System.out.println("last name of client" + clientNum + " is:" + lastname);
        file_utility.record_events("last name of client" + clientNum + " is:" + lastname);
        username = usefulmethods.read_message(in);
        if (username_is_valid(username)) {
            System.out.println("username of client" + clientNum + " is:" + username);
            file_utility.record_events("username of client" + clientNum + " is:" + username);
        } else {
            System.out.println("The username that client" + clientNum + " entered is duplicate");
            file_utility.record_events("The username that client" + clientNum + " entered is duplicate");
            sign1 = false;
        }
        password = usefulmethods.read_message(in);
        System.out.println("password of client" + clientNum + " is:" + password);
        file_utility.record_events("password of client" + clientNum + " is:" + password);
        int year = 0;
        int month = 0;
        int day = 0;
        year = Integer.parseInt(usefulmethods.read_message(in));
        System.out.println("birth_year of client" + clientNum + " is:" + year);
        file_utility.record_events("birth_year of client" + clientNum + " is:" + year);
        month = Integer.parseInt(usefulmethods.read_message(in));
        if (month > 12) {
            System.out.println("The month that client" + clientNum + " entered is invalid");
            file_utility.record_events("The month that client" + clientNum + " entered is invalid");
            sign2 = false;

        } else {
            System.out.println("birth_month of client" + clientNum + " is:" + month);
            file_utility.record_events("birth_month of client" + clientNum + " is:" + month);
        }
        day = Integer.parseInt(usefulmethods.read_message(in));
        if (day > 30) {
            System.out.println("The day that client" + clientNum + " entered is invalid");
            file_utility.record_events("The day that client" + clientNum + " entered is invalid");
            sign2 = false;

        } else {
            System.out.println("birthday of client" + clientNum + " is:" + day);
            file_utility.record_events("birthday of client" + clientNum + " is:" + day);
        }
        if (sign2)
            birthDate = LocalDate.of(year, month, day);
        bio = usefulmethods.read_message(in);
        if (bio.length() <= 256) {
            System.out.println("Bio of client" + clientNum + " is: " + bio);
            file_utility.record_events("Bio of client" + clientNum + " is: " + bio);
        } else {
            System.out.println("The String that client" + clientNum + " entered is very long .maximum valid length is 256");
            file_utility.record_events("The String that client" + clientNum + " entered is very long .maximum valid length is 256");
            sign1 = false;
        }
        if (sign1 && sign2) {
            try {
                User user = new User(firstname, lastname, username, password, birthDate, LocalDate.now(), bio);
                file_utility.add_user(user);
                users.add(user);
                usefulmethods.send_message(out, ("true"));
                j = (users.size() - 1);
                Thread.sleep(100);
                usefulmethods.send_message(out, "" + j);
                System.out.println(user.getUsername() + " got " + (j + 1) + "th user between users");
                file_utility.record_events(user.getUsername() + " got " + (j + 1) + "th user between users");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            usefulmethods.send_message(out, ("false"));
        }
    }

    /**
     * this method does sign in process
     */
    public void signIn() {
        String username;
        String password;
        boolean flag1 = false;
        boolean flag2 = false;
        System.out.println("process of client" + clientNum + " sign in started");
        file_utility.record_events("process of client" + clientNum + " sign in started");
        username = usefulmethods.read_message(in);
        password = usefulmethods.read_message(in);
        int i;
        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                System.out.println("The username that client" + clientNum + " entered (" + username + ") was existed");
                file_utility.record_events("The username that client" + clientNum + " entered (" + username + ") was existed");
                flag1 = true;
                if (password.equals(users.get(i).getPassword())) {
                    System.out.println("The password that client" + clientNum + " entered (" + password + ") was correct and signed in its account");
                    flag2 = true;
                    j = i;
                } else {
                    System.out.println("The password that client" + clientNum + " entered (" + password + ") was incorrect");
                    file_utility.record_events("The password that client" + clientNum + " entered (" + password + ") was incorrect");
                }
                break;
            }
        }
        if (!flag1) {
            System.out.println("The username that client" + clientNum + " entered (" + username + ") was not in system");
            file_utility.record_events("The username that client" + clientNum + " entered (" + username + ") was not in system ");
        }
        if (flag1 && flag2) {
            usefulmethods.send_message(out, "True");
        } else {
            usefulmethods.send_message(out, "False");
        }
    }

    /**
     * this method checks that the username which person inserted is duplicate or not
     *
     * @param username username which is checked that is duplicate or not
     * @return true if it not be duplicate otherwise false
     */
    private boolean username_is_valid(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return false;
        return true;
    }
}
