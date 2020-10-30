package server;

import report.TaskCompleteReport;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UpdateThread extends Thread {

    private final AcceptThread acceptThread;
    private int currentDifficulty = 0;
    public static boolean stopFlag = false;

    public UpdateThread(AcceptThread acceptThread) {
        this.acceptThread = acceptThread;
        acceptThread.start();
    }

    public void stopAcceptThread() {
        acceptThread.stop();
    }

    @Override
    public void run() {
        while (true) {
            runUpdates();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("@@@UpdateThread interrupted");
            }
        }
    }

    public boolean allClientsDisconnected() {
        return SocketSync.size(acceptThread.acceptedSockets) == 0;
    }

    private void runUpdates() {
        for (int i = 0; i < SocketSync.size(acceptThread.acceptedSockets); i++) {
            Socket currentSocket = SocketSync.get(i, acceptThread.acceptedSockets);
            /*send command*/ for (int j = 0; j < ReportContainer.size(); j++) {
                TaskCompleteReport currentReport = ReportContainer.get(j);
                if (currentReport.getDifficulty() == currentDifficulty) {
                    ++currentDifficulty;
                }
            }
            if (stopFlag) {
                try {
                    new ObjectOutputStream(currentSocket.getOutputStream()).writeObject("stop");
                } catch (IOException e) {
                    System.out.println(acceptThread.getName() + " disconnected with " + currentSocket.getRemoteSocketAddress());
                    SocketSync.remove(currentSocket, acceptThread.acceptedSockets);
                }
            } else {
                try {
                    new ObjectOutputStream(currentSocket.getOutputStream()).writeObject("work_" + currentDifficulty);
                } catch (IOException e) {
                    System.out.println(acceptThread.getName() + " disconnected with " + currentSocket.getRemoteSocketAddress());
                    SocketSync.remove(currentSocket, acceptThread.acceptedSockets);
                }
            }
        }
    }
}
