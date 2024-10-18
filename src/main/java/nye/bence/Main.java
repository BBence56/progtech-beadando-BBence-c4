package nye.bence;
import nye.bence.connect4.game.*;
import nye.bence.connect4.user.*;
import nye.bence.connect4.utils.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while(true){
            System.out.println("Connect 4");
            System.out.println("1. Start game");
            System.out.println("2. Exit");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if(choice == 1){
                Player player = new Player("Player");
                Game game = new Game(1);//később meg kell változtatni az ab implementálása miatt
                while(true){
                    System.out.println("Player's move");
                    System.out.println("Enter a number between 0 and 6");
                    game.getBoard().printBoard(game.getBoard().getBoard());
                    int x = scanner.nextInt();
                    if(player.place(x, game.getBoard().getBoard(), Board.SIZE_Y)){
                        System.out.println("Player wins");
                        break;
                    }
                    game.getBoard().printBoard(game.getBoard().getBoard());
                    System.out.println("Bot's move");
                    if(game.botMove(game.getBoard().getBoard(), Board.SIZE_Y, Board.SIZE_X)){
                        System.out.println("Bot wins");
                        break;
                    }

                }
            }else if(choice == 2){
                scanner.close();
                break;
            }
        }
    }
}

