package main.java.org.ce.ap.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * this class has some useful methods
 *
 * @author ashkan_mogharab
 */
public class usefulMethods {
    /**
     * this method hashes a string to an array of bytes
     *
     * @param input an string
     * @return hash of the string
     */
    public byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param hash input of toHexString method with array of bytes form
     * @return hex string of password
     */
    public String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /**
     * this method sends a message to its destination
     *
     * @param out    an output stream
     * @param string a message
     */
    public void send_message(OutputStream out, String string) {
        try {
            out.write(string.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method reads a message from its source
     *
     * @param in an input stream
     * @return message
     */
    public String read_message(InputStream in) {
        String message = null;
        try {
            byte[] buffer = new byte[2048];
            int read = in.read(buffer);
            new String(buffer, 0, read);
            message = new String(buffer, 0, read);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return message;
    }

    /**
     * this method Syncs users that read from database
     *
     * @param users users of the server
     */
    public void sync(ArrayList<User> users) {
        for (User uSer : users) {
            for (Tweet tweet : uSer.getTweets())
                if (tweet.getSender().getUsername().equals(uSer.getUsername())) {
                    for (User user : users)
                        for (int i = 0; i < user.getTweets().size(); i++) {
                            if (equal(user.getTweets().get(i), tweet) && !(user.getUsername().equals(uSer.getUsername()))) {
                                user.getTweets().set(i, tweet);
                                for (int j = 0; j < tweet.getRetweets().size(); j++)
                                    if (tweet.getRetweets().get(j).getUsername().equals(user.getUsername())) {
                                        tweet.getRetweets().set(j, user);
                                        break;
                                    }
                                break;
                            }
                        }
                    for (User user : users)
                        for (int i = 0; i < user.getLiked().size(); i++) {
                            if (equal(user.getLiked().get(i), tweet)) {
                                user.getLiked().set(i, tweet);
                                for (int j = 0; j < tweet.getLikes().size(); j++)
                                    if (tweet.getLikes().get(j).getUsername().equals(user.getUsername())) {
                                        tweet.getLikes().set(j, user);
                                        break;
                                    }
                                break;
                            }
                        }
                }
            for (User user : users)
                for (int i = 0; i < user.getFollowers().size(); i++)
                    if (user.getFollowers().get(i).getUsername().equals(uSer.getUsername())) {
                        user.getFollowers().set(i, uSer);
                        for (int j = 0; j < uSer.getFavoriteUsers().size(); j++)
                            if (uSer.getFavoriteUsers().get(j).getUsername().equals(user.getUsername())) {
                                uSer.getFavoriteUsers().set(j, user);
                                break;
                            }
                        break;
                    }


        }
    }

    /**
     * this method checks that two tweets are equal or not
     *
     * @param tweet1 a tweet
     * @param tweet2 another tweet
     * @return returns true if they be equal else returns false
     */
    private boolean equal(Tweet tweet1, Tweet tweet2) {
        return (tweet1.getSender().getUsername().equals(tweet2.getSender().getUsername()) && tweet1.getText().equals(tweet2.getText()) && tweet1.getSendDate().isEqual(tweet2.getSendDate()));
    }
}

