package Model;

import java.io.*;
import java.net.Socket;

public class SendFileThread extends Thread{
    private String fileName;
    private Socket socket = null;
    public SendFileThread(Socket socket,String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        File file = new File("file59.jpg");
        // Get the size of the file
        long length = file.length();
        byte[] bytes = new byte[16 * 1024];
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        while (true) {
            try {
                if (!((count = in.read(bytes)) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.write(bytes, 0, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
