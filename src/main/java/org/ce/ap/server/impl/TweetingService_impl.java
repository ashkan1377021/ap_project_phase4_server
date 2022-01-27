package main.java.org.ce.ap.server.impl;

import main.java.org.ce.ap.server.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class has Methods that can add ,remove ,like and retweet a tweet
 *
 * @author ashkan_mogharab
 * @version version 1 of TweetingService implementation
 */
public class TweetingService_impl implements TweetingService {
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
    // an output stream
    private OutputStream out;

    /**
     * creates a new tweeting service
     *
     * @param users            users of ServerSide
     * @param index            index of the user who wants to use tweeting service
     * @param connectionSocket a socket for connecting  to user
     */
    public TweetingService_impl(ArrayList<User> users, int index, Socket connectionSocket) {
        this.users = users;
        this.index = index;
        this.connectionSocket = connectionSocket;
        act();
    }

    /**
     * this method handles the works that should be done in tweeting service
     */
    public void act() {
        try {
            out = connectionSocket.getOutputStream();
            in = connectionSocket.getInputStream();
            String msg;
            System.out.println(users.get(index).getUsername() + " came to Tweeting service");
            file_utility.record_events(users.get(index).getUsername() + " came to Tweeting service");
            System.out.println("receive from " + users.get(index).getUsername() + " : " + (msg = usefulmethods.read_message(in)));
            file_utility.record_events("receive from " + users.get(index).getUsername() + " : " + msg);
            switch (msg) {
                case "1":
                    add();
                    break;
                case "2":
                    remove();
                    break;
                case "3":
                    like();
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
     * this method adds a tweet to user's tweets
     */
    public void add() throws InterruptedException {
        String select = usefulmethods.read_message(in);
        if (select.equals("1")) {
            System.out.println("process of add a tweet by " + users.get(index).getUsername() + "  started");
            file_utility.record_events("process of add a tweet by " + users.get(index).getUsername() + "  started");
            String text;
            int flag = 0;
            text = usefulmethods.read_message(in);
            if (text.length() <= 256) {
                System.out.println("the Tweet (" + text + ") created by " + users.get(index).getUsername());
                file_utility.record_events("the Tweet (" + text + ") created by " + users.get(index).getUsername());
                usefulmethods.send_message(out, "true");
                flag = 1;
                Thread.sleep(1);
            } else {
                usefulmethods.send_message(out, "false");
                System.out.println("the length of the text that " + users.get(index).getUsername() + " wanted to tweet was more than 256");
                file_utility.record_events("the length of the text that " + users.get(index).getUsername() + " wanted to tweet was more than 256");
            }
            if (flag == 1) {
                Tweet new_tweet = new Tweet(users.get(index), text, java.time.LocalDateTime.now());
                users.get(index).getTweets().add(new_tweet);
                users.get(index).getTweets().sort(new Sort_by_sendTime());
                file_utility.make_changes(users);
            }
        } else if (select.equals("2")) {
            System.out.println("process of retweet a tweet by " + users.get(index).getUsername() + "  started");
            file_utility.record_events("process of retweet a tweet by " + users.get(index).getUsername() + "  started");
            int ix1 = -1;
            int ix2 = -1;
            String goalUser = (usefulmethods.read_message(in));
            String textOfTweet = usefulmethods.read_message(in);
            String sendTime = usefulmethods.read_message(in);
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(goalUser)) {
                    ix1 = i;
                    break;
                }
            }
            for (int j = 0; j < users.get(ix1).getTweets().size(); j++) {
                if (users.get(ix1).getTweets().get(j).getSender().getUsername().equals(goalUser)
                        && users.get(ix1).getTweets().get(j).getText().equals(textOfTweet)
                        && users.get(ix1).getTweets().get(j).getSendDate().toString().equals(sendTime)) {
                    ix2 = j;
                    break;
                }

            }
            System.out.println(users.get(index).getUsername() + " wants to retweet from user " + goalUser);
            file_utility.record_events(users.get(index).getUsername() + " wants to retweet from user " + goalUser);
            System.out.println(users.get(index).getUsername() + " wants to retweet " + (ix2 + 1) + "th tweet of " + goalUser);
            file_utility.record_events(users.get(index).getUsername() + " wants to retweet " + (ix2 + 1) + "th tweet of " + goalUser);
            ArrayList<String> retweets = new ArrayList<>();
            for (User user : users.get(ix1).getTweets().get(ix2).getRetweets())
                retweets.add(user.getUsername());
            if (retweets.contains(users.get(index).getUsername())) {
                System.out.println(users.get(index).getUsername() + "have already retweeted this tweet");
                file_utility.record_events(users.get(index).getUsername() + "have already retweeted this tweet");
                usefulmethods.send_message(out, "false");
            } else {
                users.get(ix1).getTweets().get(ix2).getRetweets().add(users.get(index));
                users.get(index).getTweets().add(users.get(ix1).getTweets().get(ix2));
                users.get(index).getTweets().sort(new Sort_by_sendTime());
                file_utility.make_changes(users);
                System.out.println("reTweeting process by " + users.get(index).getUsername() + " successfully done");
                file_utility.record_events("reTweeting process by " + users.get(index).getUsername() + " successfully done");
                usefulmethods.send_message(out, "true");
                Thread.sleep(100);
                usefulmethods.send_message(out, "" + users.get(ix1).getTweets().get(ix2).getRetweets().size());
            }
            Thread.sleep(1);
        }
    }

    /**
     * this method removes a tweet or retweet from user's tweets or retweets
     */
    public void remove() throws InterruptedException {
        System.out.println("process of remove a tweet by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of remove a tweet by " + users.get(index).getUsername() + "  started");
        int ix = -1;
        String textOfTweet = usefulmethods.read_message(in);
        String sendTime = usefulmethods.read_message(in);
        for (int i = 0; i < users.get(index).getTweets().size(); i++) {
            if (users.get(index).getTweets().get(i).getSender().getUsername().equals(users.get(index).getUsername()) &&
                    users.get(index).getTweets().get(i).getText().equals(textOfTweet) &&
                    users.get(index).getTweets().get(i).getSendDate().toString().equals(sendTime)) {
                ix = i;
            }
        }

        System.out.println("the tweet(" + users.get(index).getTweets().get(ix).getText() + ") removed by " + users.get(index).getUsername());
        file_utility.record_events("the tweet(" + users.get(index).getTweets().get(ix).getText() + ") removed by " + users.get(index).getUsername());
        ArrayList<User> likes = users.get(index).getTweets().get(ix).getLikes();
        ArrayList<User> retweets = users.get(index).getTweets().get(ix).getRetweets();
        for (User like : likes) {
            for (int i = 0; i < like.getLiked().size(); i++)
                if (like.getLiked().get(i).equals(users.get(index).getTweets().get(ix))) {
                    like.getLiked().remove(i);
                    break;
                }
        }
        for (User retweet : retweets) {
            for (int i = 0; i < retweet.getTweets().size(); i++)
                if (retweet.getTweets().get(i).equals(users.get(index).getTweets().get(ix))) {
                    retweet.getTweets().remove(i);
                    break;
                }
        }
        users.get(index).getTweets().remove(ix);
        file_utility.make_changes(users);
        Thread.sleep(1);
    }

    /**
     * * this method likes a tweet from a user's tweets
     */
    public void like() throws InterruptedException {
        System.out.println("process of like a tweet by " + users.get(index).getUsername() + "  started");
        file_utility.record_events("process of like a tweet by " + users.get(index).getUsername() + "  started");
        int ix1 = -1;
        int ix2 = -1;
        String goalUser = usefulmethods.read_message(in);
        String textOfTweet = usefulmethods.read_message(in);
        String sendTime = usefulmethods.read_message(in);
        for (int j = 0; j < users.size(); j++) {
            if (users.get(j).getUsername().equals(goalUser)) {
                ix1 = j;
                break;
            }
        }
        for (int i = 0; i < users.get(ix1).getTweets().size(); i++) {
            if (users.get(ix1).getTweets().get(i).getSender().getUsername().equals(goalUser)
                    && users.get(ix1).getTweets().get(i).getText().equals(textOfTweet)
                    && users.get(ix1).getTweets().get(i).getSendDate().toString().equals(sendTime)) {
                ix2 = i;
                break;
            }

        }
        System.out.println(users.get(index).getUsername() + " wants to like a tweet from " + goalUser);
        file_utility.record_events(users.get(index).getUsername() + " wants to like a tweet from " + goalUser);
        System.out.println(users.get(index).getUsername() + " wants to like " + (ix2 + 1) + "th tweet of " + goalUser);
        file_utility.record_events(users.get(index).getUsername() + " wants to like " + (ix2 + 1) + "th tweet of " + goalUser);
        ArrayList<String> likes = new ArrayList<>();
        for (User user : users.get(ix1).getTweets().get(ix2).getLikes())
            likes.add(user.getUsername());
        if (likes.contains(users.get(index).getUsername())) {
            System.out.println(users.get(index).getUsername() + "have already liked this tweet");
            file_utility.record_events(users.get(index).getUsername() + "have already liked this tweet");
            usefulmethods.send_message(out, "false");
        } else {
            users.get(ix1).getTweets().get(ix2).getLikes().add(users.get(index));
            users.get(index).getLiked().add(users.get(ix1).getTweets().get(ix2));
            users.get(index).getLiked().sort(new Sort_by_sendTime());
            file_utility.make_changes(users);
            System.out.println("like a tweet process by " + users.get(index).getUsername() + " successfully done");
            file_utility.record_events("like a tweet process by " + users.get(index).getUsername() + " successfully done");
            usefulmethods.send_message(out, "true");
            Thread.sleep(100);
            usefulmethods.send_message(out, "" + users.get(ix1).getTweets().get(ix2).getLikes().size());
        }
        Thread.sleep(1);
    }
}
