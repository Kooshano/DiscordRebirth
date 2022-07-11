package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * creates new server Thread
 */
public class Server {
    static boolean running = true;
    private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        ServerThread serverThread = new ServerThread();
        serverThread.start();
        while(true){
            String inp = scanner.nextLine();
            if(inp.equals("shutdown")){
                System.out.println("Server is shutting down");
                running = false;
                Data.saveData();
                Data.saveGroups();
                Data.saveUsers();
                serverThread.shutDown();
                serverThread.interrupt();
                return;
            }
        }
    }

    public static ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public static boolean isRunning() {
        return running;
    }
}
