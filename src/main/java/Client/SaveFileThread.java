package Client;//package Client;
//
//import java.io.*;
//import java.net.Socket;
//
//public class SaveFileThread extends Thread{
//    Socket fileSocket;
//
//    @Override
//    public void run() {
//        ObjectInputStream profileInputStream = new ObjectInputStream(profileSocket.getInputStream());
//        ObjectOutputStream profileOutputStream = new ObjectOutputStream(profileSocket.getOutputStream());
//        OutputStream profile = new FileOutputStream(client.getUsername() + ".png");
//        byte[] bytes = new byte[16 * 1024];
//        int count;
//        while ((count = profileInputStream.read(bytes)) > 0) {
//            try {
//                profileOutputStream.write(bytes, 0, count);
//                profile.close();
//                profileInputStream.close();
//                profileOutputStream.close();
//                fileSocket.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//
//
//    }
//}
