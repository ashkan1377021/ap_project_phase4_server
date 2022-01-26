package main.java.org.ce.ap.server.impl;

import main.java.org.ce.ap.server.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class has Methods that a user can observe its favorite users Tweets with their likes and reTweets
 *
 * @author ashkan_mogharab
 * @version version 1 of TimelineService implementation
 */
public class TimelineService_impl implements TimelineService {
    //a socket for connecting  to user
    private final Socket connectionSocket;
    //an object of usefulMethods
    private final usefulMethods usefulmethods = new usefulMethods();
    //users of ServerSide
    private final ArrayList<User> users;
    //index of the user who wants to observe its favorite users Tweets with their likes and reTweets
    private final int index;
    // an object of File_utility
    File_utility file_utility = new File_utility();

    /**
     * creates a new timeline service
     *
     * @param users            users of ServerSide
     * @param index            index of the user who wants to observe its favorite users Tweets with their likes and reTweets
     * @param connectionSocket a socket for connecting  to user
     */
    public TimelineService_impl(ArrayList<User> users, int index, Socket connectionSocket) throws InterruptedException {
        this.users = users;
        this.index = index;
        this.connectionSocket = connectionSocket;
        showTweets();
    }

    /**
     * this method shows Tweets,reTweets and likes of each favorite user to it and also tweets of itself
     */
    public void showTweets() {
        try {
            OutputStream out = connectionSocket.getOutputStream();
            System.out.println("process of observe Timeline by " + users.get(index).getUsername() + "  started");
            file_utility.record_events("process of observe Timeline by " + users.get(index).getUsername() + "  started");
            ArrayList<Tweet> ownTweets = new ArrayList<>();
            for (Tweet tweet : users.get(index).getTweets()) {
                if (tweet.getSender().equals(users.get(index)))
                    ownTweets.add(tweet);
            }
            usefulmethods.send_message(out, "" + ownTweets.size());
            Thread.sleep(100);
            for (Tweet tweet : ownTweets) {
                usefulmethods.send_message(out, tweet.getSender().getUsername());
                Thread.sleep(100);
                usefulmethods.send_message(out, "" + tweet.getRetweets().size());
                Thread.sleep(100);
                usefulmethods.send_message(out, "" + tweet.getLikes().size());
                Thread.sleep(100);
                usefulmethods.send_message(out, "" + tweet.getText());
                Thread.sleep(100);
                usefulmethods.send_message(out, tweet.getSendDate().getYear() + "\\" + tweet.getSendDate().getMonth() + "\\" + tweet.getSendDate().getDayOfMonth() + "  " + tweet.getSendDate().getHour() + ":" + tweet.getSendDate().getMinute() + ":" + +tweet.getSendDate().getSecond());
                Thread.sleep(100);
            }
            int count1 = users.get(index).getFavoriteUsers().size();
            usefulmethods.send_message(out, "" + count1);
            Thread.sleep(100);
            for (User favorite : users.get(index).getFavoriteUsers()) {
                ArrayList<Tweet> tweets = new ArrayList<>();
                ArrayList<Tweet> retweets = new ArrayList<>();
                ArrayList<Tweet> likedTweets = favorite.getLiked();
                ArrayList<Tweet> favoriteUserTweets = favorite.getTweets();
                for (Tweet tweet : favoriteUserTweets) {
                    if (tweet.getSender().equals(favorite))
                        tweets.add(tweet);
                    else {
                        retweets.add(tweet);
                    }
                }
                usefulmethods.send_message(out, "" + tweets.size());
                Thread.sleep(101);
                for (Tweet tweet : tweets) {
                    usefulmethods.send_message(out, tweet.getSendDate().getYear() + "\\" + tweet.getSendDate().getMonth() + "\\" + tweet.getSendDate().getDayOfMonth() + "  " + tweet.getSendDate().getHour() + ":" + tweet.getSendDate().getMinute() + ":" + +tweet.getSendDate().getSecond());
                    Thread.sleep(101);
                    usefulmethods.send_message(out, "" + tweet.getRetweets().size());
                    Thread.sleep(101);
                    usefulmethods.send_message(out, "" + tweet.getLikes().size());
                    Thread.sleep(101);
                    usefulmethods.send_message(out, "" + tweet.getText());
                    Thread.sleep(101);
                    usefulmethods.send_message(out, tweet.getSender().getUsername());
                    Thread.sleep(101);
                }
                usefulmethods.send_message(out, "" + retweets.size());
                Thread.sleep(102);
                for (Tweet tweet : retweets) {
                    usefulmethods.send_message(out, favorite.getUsername());
                    Thread.sleep(102);
                    usefulmethods.send_message(out, tweet.getSender().getUsername());
                    Thread.sleep(102);
                    usefulmethods.send_message(out, tweet.getSendDate().getYear() + "\\" + tweet.getSendDate().getMonth() + "\\" + tweet.getSendDate().getDayOfMonth() + "  " + tweet.getSendDate().getHour() + ":" + tweet.getSendDate().getMinute() + ":" + +tweet.getSendDate().getSecond());
                    Thread.sleep(102);
                    usefulmethods.send_message(out, "" + tweet.getRetweets().size());
                    Thread.sleep(102);
                    usefulmethods.send_message(out, "" + tweet.getLikes().size());
                    Thread.sleep(102);
                    usefulmethods.send_message(out, "" + tweet.getText());
                    Thread.sleep(102);
                }
                usefulmethods.send_message(out, "" + likedTweets.size());
                Thread.sleep(103);
                for (Tweet liked : likedTweets) {
                    usefulmethods.send_message(out, favorite.getUsername());
                    Thread.sleep(103);
                    usefulmethods.send_message(out, liked.getSender().getUsername());
                    Thread.sleep(103);
                    usefulmethods.send_message(out, liked.getSendDate().getYear() + "\\" + liked.getSendDate().getMonth() + "\\" + liked.getSendDate().getDayOfMonth() + "  " + liked.getSendDate().getHour() + ":" + liked.getSendDate().getMinute() + ":" + +liked.getSendDate().getSecond());
                    Thread.sleep(103);
                    usefulmethods.send_message(out, "" + liked.getRetweets().size());
                    Thread.sleep(103);
                    usefulmethods.send_message(out, "" + liked.getLikes().size());
                    Thread.sleep(103);
                    usefulmethods.send_message(out, "" + liked.getText());
                    Thread.sleep(103);
                }
            }
            System.out.println("process of observe Timeline by " + users.get(index).getUsername() + " ended");
            file_utility.record_events("process of observe Timeline by " + users.get(index).getUsername() + " ended");
            connectionSocket.close();
            out.close();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}

