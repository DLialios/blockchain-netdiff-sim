package client;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        System.out.print("IP Address\n-->");
        String serverIP = new Scanner(System.in).nextLine();
        MainThread t0 = new MainThread(serverIP);
        t0.start();
        while (!new Scanner(System.in).next().equals("stop")) {
        }
        System.out.println("Exiting...");
        System.exit(0);
    }
}
