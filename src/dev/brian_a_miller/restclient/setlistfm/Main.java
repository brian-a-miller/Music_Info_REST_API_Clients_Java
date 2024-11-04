package com.myalbumdj.restclient.setlistfm;

import com.myalbumdj.restclient.setlistfm.data.Setlist;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private final static String OUTPUT_FILE_NAME = "concerts_attended.txt";

    public static void main(String[] args) {

        SetlistFMClient setlistFMClient = SetlistFMClient.getInstance();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter setlist.fm user name: ");
        String username = scanner.nextLine();

        var concertsAttended = setlistFMClient.getConcertsAttendedByUser(username);
        //"bamjuggler");

        if (concertsAttended != null) {
            if (concertsAttended.setlist == null) {
                System.out.println("concertsAttended.setlist == null");
            } else {
                System.out.println("concertsAttended.setlist.size() = " + concertsAttended.setlist.size());

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME, true))) {
                    for (Setlist concert : concertsAttended.setlist) {
                        bw.write(concert.eventDate());
                        bw.write("\t");
                        bw.write(concert.artist().name());
                        bw.write("\t");
                        bw.write(concert.venue().name());
                        bw.write("\t");
                        bw.write(concert.venue().getCityName());
                        bw.write("\t");
                        bw.write(concert.venue().getStateCode());
                        bw.write("\t");
                        bw.write(concert.venue().getCountryCode());
                        bw.newLine();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
            }
        } else {
            System.err.println("concertsAttended == null");
        }
    }
}
