package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        long start = System.nanoTime();
        System.out.println("Starting AcceptThread t0 for issuing commands...");
        System.out.println("Starting UpdateThread t1 for issuing commands...");
        AcceptThread t0 = new AcceptThread(48396, "Thread-0 connection established with");
        UpdateThread t1 = new UpdateThread(t0);
        t1.start();
        System.out.println("Starting AcceptThread t2 for listening...");
        System.out.println("Starting ListenThread t3 for listening...");
        AcceptThread t2 = new AcceptThread(48397, "Thread-2 connection established with");
        ListenThread t3 = new ListenThread(t2);
        t3.start();
        while (!new Scanner(System.in).next().equals("stop")) {
        }
        long duration = System.nanoTime() - start;
        UpdateThread.stopFlag = true;
        t1.stopAcceptThread();
        t3.stopAcceptThread();
        while (!t1.allClientsDisconnected()) {
        }
        while (!t3.allClientsDisconnected()) {
        }
        System.out.println("Printing results");
        for (int i = 0; i < ReportContainer.size(); i++) {
            System.out.println(ReportContainer.get(i));
        }
        writeToFile("output.txt", duration);
        System.out.println("Results written to file");
        System.out.print("Press enter to exit");
        new Scanner(System.in).nextLine();
        System.exit(0);
    }

    public static void writeToFile(String fileName, long executionTimeInNano) {
        String executionTimeInSec = new DecimalFormat("0.00").format(executionTimeInNano * Math.pow(10, -9));
        Writer fileWriter;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println("Could not create " + fileName);
            return;
        }
        try {
            fileWriter.write("Uptime: " + executionTimeInSec + "s\n");
            for (int i = 0; i < ReportContainer.size(); i++) {
                fileWriter.write(ReportContainer.get(i).toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to write to " + fileName);
        }
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Could not flush Writer stream for " + fileName);
        }
    }
}
