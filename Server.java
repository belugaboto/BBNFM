
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;


public class Server {


    private static ServerSocket server;
    private static int port = 8888;

    public static void main(String args[]) throws ClassNotFoundException {
        
        int i =0;
        Thread thread = null;
        try {
            server = new ServerSocket(port);

            while(true){
                System.out.println("\nWaiting for the Player");
                Socket socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println(message);
                Random rand = new Random();
                int newPort = rand.nextInt(9000)+1000;
                Dictionary d = new Dictionary(newPort);
                thread = new Thread(d);
                thread.start();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(""+newPort);

                ois.close();
                oos.close();
                socket.close();
                i++;

            }
        } catch (IOException ex) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("ERROR closing socket: " + e.getMessage());
            }
        }
    }

}
