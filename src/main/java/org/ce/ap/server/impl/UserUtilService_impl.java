package main.java.org.ce.ap.server.impl;

import main.java.org.ce.ap.server.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * this class can send information of a user and also set image for a user
 *
 * @author ashkan_mogharab
 * @version version 1 of UserUtilService implementation
 */
public class UserUtilService_impl implements UserUtilService {
    //a socket for connecting  to user
    private final Socket connectionSocket;
    //an object of usefulMethods
    private final usefulMethods usefulmethods = new usefulMethods();
    //users of ServerSide
    private final ArrayList<User> users;
    //index of the user who wants to use from UserUtil service
    private final int index;
    // an object of File_utility
    File_utility file_utility = new File_utility();
    // an input stream
    private InputStream in;
    // an output stream
    private OutputStream out;

    /**
     * creates a new UserUtil service
     *
     * @param index            index of the user
     * @param users            users of ServerSide
     * @param connectionSocket a socket for connecting  to user
     */
    public UserUtilService_impl(ArrayList<User> users, int index, Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        this.users = users;
        this.index = index;
        act();
    }

    /**
     * this method handles the works that should be done in UserUtil service
     */
    public void act() {
        try {
            out = connectionSocket.getOutputStream();
            in = connectionSocket.getInputStream();
            String msg;
            System.out.println(users.get(index).getUsername() + " came to UserUtil service");
            file_utility.record_events(users.get(index).getUsername() + " came to UserUtil service");
            System.out.println("receive from " + users.get(index).getUsername() + " : " + (msg = usefulmethods.read_message(in)));
            file_utility.record_events("receive from " + users.get(index).getUsername() + " : " + msg);
            switch (msg) {
                case "1":
                    setImage();
                    break;
                case "2":
                    sendInformation();
                    break;
                case "3":
                    checkUsernameExistence();
                    break;

            }
            connectionSocket.close();
            out.close();
            in.close();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method sets a picture for the user
     */
    public void setImage() throws IOException {
        System.out.println("process of set image by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of set image by " + users.get(index).getUsername() + "  started");
        byte[] sizeAr = new byte[4];
        in.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        in.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        users.get(index).setImage(image);
        System.out.println("process of set image by " + users.get(index).getUsername() + " ended");
        file_utility.record_events("process of set image by " + users.get(index).getUsername() + " ended");
        file_utility.make_changes(users);
    }

    /**
     * this method send information of a user to the user
     */
    public void sendInformation() throws IOException, InterruptedException {
        String sign = usefulmethods.read_message(in);
        String sign2 = usefulmethods.read_message(in);
        String goalUser = usefulmethods.read_message(in);
        System.out.println("process of receive information of " + goalUser + " by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of receive information of " + goalUser + " by " + users.get(index).getUsername() + "  started");
        int ix1 = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(goalUser)) {
                ix1 = i;
            }
        }
        sendImage(out, users.get(ix1).getImage());
        usefulmethods.send_message(out, users.get(ix1).getFirstname());
        Thread.sleep(100);
        usefulmethods.send_message(out, users.get(ix1).getUsername());
        Thread.sleep(100);
        usefulmethods.send_message(out, users.get(ix1).getBio());
        Thread.sleep(100);
        usefulmethods.send_message(out, "" + users.get(ix1).getFavoriteUsers().size());
        Thread.sleep(100);
        usefulmethods.send_message(out, "" + users.get(ix1).getFollowers().size());
        Thread.sleep(100);
        if (sign2.equals("2")) {
            if (users.get(ix1).getFollowers().contains(users.get(index))) {
                usefulmethods.send_message(out, "true");

            } else {
                usefulmethods.send_message(out, "false");
            }
            Thread.sleep(100);
        }
        ArrayList<Tweet> tweetsOrLiked;
        if (sign.equals("1")) {
            tweetsOrLiked = users.get(ix1).getTweets();

        } else {
            tweetsOrLiked = users.get(ix1).getLiked();
        }
        usefulmethods.send_message(out, "" + tweetsOrLiked.size());
        Thread.sleep(100);
        for (Tweet tweet : tweetsOrLiked) {
            if (tweet.getSender().getUsername().equals(users.get(ix1).getUsername())) {
                usefulmethods.send_message(out, "true");
            } else {
                usefulmethods.send_message(out, "false");
            }
            Thread.sleep(100);
            usefulmethods.send_message(out, tweet.getSender().getUsername());
            Thread.sleep(100);
            usefulmethods.send_message(out, "" + tweet.getRetweets().size());
            Thread.sleep(101);
            usefulmethods.send_message(out, "" + tweet.getLikes().size());
            Thread.sleep(101);
            usefulmethods.send_message(out, "" + tweet.getText());
            Thread.sleep(101);
            usefulmethods.send_message(out, tweet.getSendDate().getYear() + "\\" + tweet.getSendDate().getMonth() + "\\" + tweet.getSendDate().getDayOfMonth() + "  " + tweet.getSendDate().getHour() + ":" + tweet.getSendDate().getMinute() + ":" + +tweet.getSendDate().getSecond());
            Thread.sleep(101);
        }
        System.out.println("information of " + goalUser + " successfully sent to " + users.get(index).getUsername());
        file_utility.record_events("information of " + goalUser + " successfully sent to " + users.get(index).getUsername());
    }

    /**
     * this method sends an image
     *
     * @param out   an output stream
     * @param image the image which wants to be sent
     */
    private void sendImage(OutputStream out, BufferedImage image) throws InterruptedException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        out.write(size);
        out.write(byteArrayOutputStream.toByteArray());
        out.flush();
        Thread.sleep(1000);
    }

    /**
     * this method checks that a username exists or not
     */
    public void checkUsernameExistence() {
        String Username = usefulmethods.read_message(in);
        System.out.println("process of check existence of " + Username + " by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of check existence of " + Username + " by " + users.get(index).getUsername() + "  started");
        int ix1 = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(Username)) {
                ix1 = i;
            }
        }
        if (ix1 != -1) {
            usefulmethods.send_message(out, "true");
            System.out.println(Username + " was existed and we informed it to " + users.get(index).getUsername());
            file_utility.record_events(Username + " existed and we informed it to " + users.get(index).getUsername());
        } else {
            usefulmethods.send_message(out, "false");
            System.out.println(Username + " was not existed and we informed it to " + users.get(index).getUsername());
            file_utility.record_events(Username + " was not existed and we informed it to " + users.get(index).getUsername());
        }
    }
}

