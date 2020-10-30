package server;

import report.TaskCompleteReport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ListenThread extends Thread {

    private final AcceptThread acceptThread;

    public ListenThread(AcceptThread acceptThread) {
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
        }
    }

    public boolean allClientsDisconnected() {
        return SocketSync.size(acceptThread.acceptedSockets) == 0;
    }

    private void runUpdates() {
        for (int i = 0; i < SocketSync.size(acceptThread.acceptedSockets); i++) {
            Socket currentSocket = SocketSync.get(i, acceptThread.acceptedSockets);
            /*receive input*/ TaskCompleteReport report;
            try {
                report = (TaskCompleteReport) new ObjectInputStream(currentSocket.getInputStream()).readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(acceptThread.getName() + " disconnected with " + currentSocket.getRemoteSocketAddress());
                SocketSync.remove(currentSocket, acceptThread.acceptedSockets);
                continue;
            }
            /*process input*/ if (report.getDifficulty() != -1) {
                ReportContainer.add(report, currentSocket.getRemoteSocketAddress().toString());
            }
        }
    }
}
