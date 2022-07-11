package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * created by kooshan 2022
 */
public class ServerThread extends Thread {
    private Socket socket;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server is running on port 8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (Server.isRunning()) {
            try {
                // accept a new client connection
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void shutDown() {
        for (ClientHandler clientHandler : Server.getClients()) {
            clientHandler.exit();
        }
    }
}
