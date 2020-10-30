package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AcceptThread extends Thread {

    private final int port;
    private final String acceptMessage;
    public final ArrayList<Socket> acceptedSockets = new ArrayList<>();

    public AcceptThread(int port, String acceptMessage) {
        this.port = port;
        this.acceptMessage = acceptMessage;
    }

    @Override
    public void run() {
        ServerSocket welcome = null;
        try {
            welcome = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Failed to bind port " + port + ". Exiting...");
            System.exit(0);
        }
        while (true) {
            Socket socket;
            try {
                socket = welcome.accept();
            } catch (IOException e) {
                System.out.println("@@@Connection failure whilst creating new socket");
                continue;
            }
            System.out.println(acceptMessage + " " + socket.getRemoteSocketAddress());
            SocketSync.add(socket, acceptedSockets);
        }
    }
}
