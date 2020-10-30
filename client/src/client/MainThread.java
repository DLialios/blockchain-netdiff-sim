package client;

import report.TaskCompleteReport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class MainThread extends Thread {

    private Socket listenSocket;
    private Socket sendSocket;
    private ComputeThread currentJobThread;

    @Override
    public void run() {
        while (true) {
            runUpdates();
        }
    }

    public MainThread(String serverIP) {
        while (true) {
            try {
                listenSocket = new Socket(serverIP, 48396);
                sendSocket = new Socket(serverIP, 48397);
                break;
            } catch (IOException e) {
                System.out.println("Connection failure. Trying again in two seconds...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e2) {
                    System.out.println("@@@MainThread interrupted");
                }
            }
        }
        System.out.println("Connected to " + listenSocket.getRemoteSocketAddress());
    }

    private void runUpdates() {
        /*receive message*/ String message = null;
        try {
            message = (String) new ObjectInputStream(listenSocket.getInputStream()).readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Server connection lost. Exiting...");
            System.exit(0);
        }
        /*process the command*/ if (message.substring(0, 4).equals("work")) {
            if (!ComputeThread.hasFinishedThisJob(Integer.parseInt(message.substring(5))) && ComputeThread.isRunning == false) {
                currentJobThread = new ComputeThread(Integer.parseInt(message.substring(5)));
                currentJobThread.start();
                return;
            }
            if (ComputeThread.isRunning == true) {
                if (currentJobThread.getDifficulty() != Integer.parseInt(message.substring(5))) {
                    System.out.println("ComputeThread for difficulty " + currentJobThread.getDifficulty() + " stopped remotely");
                    currentJobThread.stop();
                    ComputeThread.isRunning = false;
                    return;
                }
                try {
                    new ObjectOutputStream(sendSocket.getOutputStream()).writeObject(new TaskCompleteReport("-1", -1, new BigInteger("-1"), "-1"));
                } catch (IOException e) {
                    System.out.println("@@@MainThread failed to send progress report");
                }
                return;
            }
        } else if (message.equals("stop")) {
            System.out.println("Termination message received. Exiting...");
            System.exit(0);
        }
        /*send answer*/ try {
            new ObjectOutputStream(sendSocket.getOutputStream()).writeObject(currentJobThread.getReport());
        } catch (IOException e) {
            System.out.println("@@@MainThread failed to send completed report");
        }
    }
}
