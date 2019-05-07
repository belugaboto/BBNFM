import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client {

    private static int MAX_LIFE = 7;
    private static int INIT_PORT = 8888;
    private static String HOST = "127.0.0.1";

    private static int miss_chance = 0;
    private static Socket socket = null;
    private static ObjectOutputStream socketOutput = null;
    private static ObjectInputStream socketInput = null;

    private static String hidden_letter = "";
    private static String missed = "";

    private static int isWin = 0;
    private static int isLose = 0;


    private static void getStatus(){
        try {
            socketOutput.writeObject("getStatus");
            String input = (String) socketInput.readObject();
            String[] res = input.split("@");  // split response with @ and store in String arrays

            // assign each status to global variable
            hidden_letter = res[0];
            miss_chance = Integer.parseInt(res[1]);
            missed = res[2];
            isWin = Integer.parseInt(res[3]);
            isLose = Integer.parseInt(res[4]);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static String getAnswer(){
        try {
            socketOutput.writeObject("getAnswer");
            String input = (String) socketInput.readObject();
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void sendUserInput(String s){
        try {
            socketOutput.writeObject("send:"+s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        Scanner keyboard;
        System.out.println("Welcome to Hangman Game");
        System.out.println("Connect to server " + HOST + " : " + INIT_PORT);
        try {

            socket = new Socket("server", INIT_PORT);
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            socketOutput.writeObject("start");


            socketInput = new ObjectInputStream(socket.getInputStream());
            String newPort = (String) socketInput.readObject();
            System.out.println("New Port : " + newPort);
            keyboard = new Scanner(System.in);


            socket = new Socket("server", Integer.parseInt(newPort));
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            socketInput = new ObjectInputStream(socket.getInputStream());

            getStatus();
            while (isWin == 0 && isLose ==0) {

                System.out.println("\n");
                System.out.println("Letter :\t" + hidden_letter);
                System.out.println("Wrong X : " + missed);
                System.out.println("\nYou have " + (MAX_LIFE - miss_chance) + " life point");
                System.out.print("Answer : ");

     
                sendUserInput(keyboard.nextLine());
                getStatus();
            }

            System.out.println("\n");

            if (isWin==1){
                System.out.println("You have " + (MAX_LIFE - miss_chance) + " life point");
                System.out.println( "Godlike, Pro is here" );
                System.out.println( "Answer : " + hidden_letter );

            }
            else if (isLose ==1) {
                String answer = getAnswer();
                System.out.println("You have " + (MAX_LIFE - miss_chance) + " life point");
                System.out.println("kids, Loser on this game");
                System.out.println("Answer : " + answer);
            }

            socketOutput.writeObject("exit");


            socketOutput.close();
            socketInput.close();
            socket.close();

        }
         catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}

