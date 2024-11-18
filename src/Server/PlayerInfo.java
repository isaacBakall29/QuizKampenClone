package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerInfo {

    private Socket socket;
    private ObjectInputStream clientObjectInputStream; //to read object coming from client
    private ObjectOutputStream clientObjectOutputStream; // to write object to the client from server

    public PlayerInfo(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.clientObjectInputStream = in;
        this.clientObjectOutputStream = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getClientObjectInputStream() {
        return clientObjectInputStream;
    }

    public void setClientObjectInputStream(ObjectInputStream clientObjectInputStream) {
        this.clientObjectInputStream = clientObjectInputStream;
    }

    public ObjectOutputStream getClientObjectOutputStream() {
        return clientObjectOutputStream;
    }

    public void setClientObjectOutputStream(ObjectOutputStream clientObjectOutputStream) {
        this.clientObjectOutputStream = clientObjectOutputStream;
    }
}
