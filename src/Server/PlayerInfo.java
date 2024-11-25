package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerInfo {

    private Socket socket;
    private ObjectInputStream clientObjectInputStream;
    private ObjectOutputStream clientObjectOutputStream;

    public PlayerInfo(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.clientObjectInputStream = in;
        this.clientObjectOutputStream = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getClientObjectInputStream() {
        return clientObjectInputStream;
    }

    public ObjectOutputStream getClientObjectOutputStream() {
        return clientObjectOutputStream;
    }

    public void writeObject(Object obj) throws IOException {
        clientObjectOutputStream.writeObject(obj);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return clientObjectInputStream.readObject();
    }
}
