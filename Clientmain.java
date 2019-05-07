
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Clientmain {
    public static void main( String[] args ) {
        Scanner keyboard;
        System.out.println("Welcome to Hangman Game");
        System.out.println("Connect to server " + HOST + " : " + INIT_PORT);
        try {
            // init socket with init port
            socket = new Socket(HOST, INIT_PORT);
            // send "start" to get new port
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            socketOutput.writeObject("start");

            // get new port to change connection
            socketInput = new ObjectInputStream(socket.getInputStream());
            String newPort = (String) socketInput.readObject();
            System.out.println("New Port : " + newPort);
            keyboard = new Scanner(System.in);

            //// switch to new port
            socket = new Socket(HOST, Integer.parseInt(newPort));
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            socketInput = new ObjectInputStream(socket.getInputStream());

            // get initial game status
            getStatus();

            while (isWin == 0 && isLose ==0) {


                // print game status
                System.out.println("\n");
                System.out.println("Letter :\t" + hidden_letter);
                System.out.println("Wrong X : " + missed);
                System.out.println("\nYou have " + (MAX_LIFE - miss_chance) + " life point");
                System.out.print("Answer : ");

                // send user input
                sendUserInput(keyboard.nextLine());

                // get new game status
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

            // send "exit" for tell server thead to close socket
            socketOutput.writeObject("exit");

            // close socket
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



