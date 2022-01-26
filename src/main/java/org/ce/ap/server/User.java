package main.java.org.ce.ap.server;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * this class holds details of a user in ServerSide
 *
 * @author ashkan_mogharab
 */
public class User implements Serializable {
    //image of the user
    private transient BufferedImage image = new image().getImage();
    //firstname of the user
    private String firstname;
    //lastname of the user
    private String lastname;
    //username of the user that should be different from others
    private String username;
    //password of the user that it should be in hash form
    private String password;
    //birthDate of the user
    private LocalDate birthDate;
    //registry Date of the user
    private LocalDate registryDate;
    //bio of the user that its  maximum length is 256
    private String bio;
    //tweets of the user
    private ArrayList<Tweet> tweets;
    //followers of the user
    private ArrayList<User> followers;
    //favorite users of the user
    private ArrayList<User> favoriteUsers;
    //liked tweets
    private ArrayList<Tweet> liked;

    /**
     * creates a new user
     *
     * @param firstname    firstname of the user
     * @param lastname     lastname of the user
     * @param username     username of the user
     * @param password     password of the user
     * @param birthDate    birthDate of the user
     * @param registryDate registryDate of the user
     * @param bio          bio of the user
     */
    public User(String firstname, String lastname, String username, String password, LocalDate birthDate, LocalDate registryDate, String bio) {
        try {
            this.firstname = firstname;
            this.lastname = lastname;
            this.username = username;
            usefulMethods usefulmethods = new usefulMethods();
            this.password = usefulmethods.toHexString(usefulmethods.getSHA(password));
            this.birthDate = birthDate;
            this.registryDate = registryDate;
            this.bio = bio;
            tweets = new ArrayList<>();
            followers = new ArrayList<>();
            favoriteUsers = new ArrayList<>();
            liked = new ArrayList<>();
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
    }

    /**
     * getter
     *
     * @return first name of the user
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * getter
     *
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * getter
     *
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter
     *
     * @return tweets of the user
     */
    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    /**
     * getter
     *
     * @return followers of the user
     */
    public ArrayList<User> getFollowers() {
        return followers;
    }

    /**
     * getter
     *
     * @return favorite users of the user
     */
    public ArrayList<User> getFavoriteUsers() {
        return favoriteUsers;
    }

    /**
     * getter
     *
     * @return liked tweets
     */
    public ArrayList<Tweet> getLiked() {
        return liked;
    }

    /**
     * getter
     *
     * @return bio of the user
     */
    public String getBio() {
        return bio;
    }

    /**
     * getter
     *
     * @return image of the user
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * setter
     *
     * @param image the image which wants to be user's image
     */

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

