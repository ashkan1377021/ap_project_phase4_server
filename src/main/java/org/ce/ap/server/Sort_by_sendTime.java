package main.java.org.ce.ap.server;
import java.util.*;
/**
 * this class with compare method,Sorts tweets in ascending order by sendTime
 */
public class Sort_by_sendTime implements Comparator<Tweet>{
    @Override
    public int compare(Tweet T1, Tweet T2) {
        return T2.getSendDate().compareTo(T1.getSendDate());
    }
}