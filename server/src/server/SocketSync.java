package server;

import java.net.Socket;
import java.util.ArrayList;

public class SocketSync {

    public static synchronized void add(Socket s, ArrayList<Socket> a) {
        a.add(s);
    }

    public static synchronized Socket get(int i, ArrayList<Socket> a) {
        return a.get(i);
    }

    public static synchronized void remove(Socket s, ArrayList<Socket> a) {
        a.remove(s);
    }

    public static synchronized int size(ArrayList<Socket> a) {
        return a.size();
    }
}
