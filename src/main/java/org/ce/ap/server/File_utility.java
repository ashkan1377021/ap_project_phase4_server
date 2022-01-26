package main.java.org.ce.ap.server;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class does things related to the file
 *
 * @author ashkan
 */
public class File_utility {
    /**
     * this method reads users from database
     *
     * @return users of server
     */
    public ArrayList<User> read_users() {
        ArrayList<User> users = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\model\\users\\names.txt");
            Scanner sc = new Scanner(fileReader);
            while (sc.hasNext()) {
                String st = sc.next();
                FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\model\\users\\" + st + ".txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                User user = (User) objectInputStream.readObject();
                user.setImage(ImageIO.read(objectInputStream));
                users.add(user);
                fileInputStream.close();
                objectInputStream.close();
            }
            sc.close();
            return users;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    /**
     * this method make changes in details of server's users
     *
     * @param users an ArrayList that will change
     */
    public void make_changes(ArrayList<User> users) {
        for (User user : users)
            edit_user(user);
    }

    /**
     * this method adds a user to server's users
     *
     * @param user a user
     */
    public void add_user(User user) {
        String st = user.getUsername();
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\model\\users\\" + st + ".txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             FileWriter fileWriter = new FileWriter("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\model\\users\\names.txt", true)) {
            st = st + "\n";
            fileWriter.write(st);
            objectOutputStream.writeObject(user);
            objectOutputStream.writeObject(ImageIO.write(user.getImage(), "jpg", objectOutputStream));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method edits information of a user
     *
     * @param user a user
     */
    private void edit_user(User user) {
        String st = user.getUsername();
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\model\\users\\" + st + ".txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(user);
            objectOutputStream.writeObject(ImageIO.write(user.getImage(), "jpg", objectOutputStream));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * this method record events in server
     *
     * @param st a string that describes an event
     */
    public void record_events(String st) {
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\ashkan mogharab\\Desktop\\temp\\Twitter\\files\\log\\log.txt", true)) {
            st = st + "\n";
            fileWriter.write(st);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
