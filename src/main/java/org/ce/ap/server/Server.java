package main.java.org.ce.ap.server;

import main.java.org.ce.ap.server.impl.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        File_utility file_utility = new File_utility();
        usefulMethods usefulmethods = new usefulMethods();
        ArrayList<User> users = file_utility.read_users();
        usefulmethods.sync(users);
        ExecutorService pool = Executors.newCachedThreadPool();
        int count = 0;
        try (ServerSocket welcomingSocket = new ServerSocket(7600)) {
            System.out.println("Server started");
            file_utility.record_events("Server started");
            while (count < 1000000) {
                Socket connectionSocket = welcomingSocket.accept();
                count++;
                pool.execute(new ClientHandler(connectionSocket, users, count));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    Socket connectionSocket;
    ArrayList<User> users;
    int clientNum;

    public ClientHandler(Socket connectionSocket, ArrayList<User> users, int clientNum) {
        this.connectionSocket = connectionSocket;
        this.users = users;
        this.clientNum = clientNum;
    }

    @Override
    public void run() {
        try {
            InputStream in = connectionSocket.getInputStream();
            usefulMethods usefulmethods = new usefulMethods();
            String select;
            int index;
            select = usefulmethods.read_message(in);
            switch (select) {
                case "1":
                    new AuthenticationService_impl(users, clientNum, connectionSocket);
                    break;
                case "2":
                    index = Integer.parseInt(usefulmethods.read_message(in));
                    new TweetingService_impl(users, index, connectionSocket);
                    break;
                case "3":
                    index = Integer.parseInt(usefulmethods.read_message(in));
                    new ObserverService_impl(users, index, connectionSocket);
                    break;
                case "4":
                    index = Integer.parseInt(usefulmethods.read_message(in));
                    new TimelineService_impl(users, index, connectionSocket);
                    break;
                case "5":
                    index = Integer.parseInt(usefulmethods.read_message(in));
                    new UserUtilService_impl(users, index, connectionSocket);
                    break;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}


