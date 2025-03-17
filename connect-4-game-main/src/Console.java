import java.util.Scanner;
public class Console {
    private Game game;

    private AiGame AiGame;
    private Scanner scanner;

    //Colors
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_TURQUOISE = "\u001B[36m";
    private final String ANSI_PINK = "\u001B[35m";
    private final String CIRCLE = "●";

    public Console(){
        this.game= new Game();
        this.scanner = new Scanner(System.in);
    }

    //game
    public void startGame() {
        boolean stillPlaying;
        do {
            this.game = new Game();
            this.AiGame = new AiGame();
            System.out.println("Welcome to Connect Four!");
            System.out.println("Select mode:");
            System.out.println("1. Two players");
            System.out.println("2. One player vs AI");

            int modeSelection = getIntInput(1, 2);
            // Play with AI
            if (modeSelection == 2)
            {
                System.out.println("Enter " + ANSI_TURQUOISE + "Player 1" + ANSI_RESET + " (" + ANSI_TURQUOISE + CIRCLE + ANSI_RESET + ") name:");
                String player1Name = scanner.next();
                do
                {
                    AiGame.AiMove();
                    AiGame.printBoard();
                    if(AiGame.checkWin())
                    {
                        break;
                    }
                    AiGame.setCurrentPlayer();
                    System.out.println(ANSI_TURQUOISE + "Turn of: " + player1Name + ANSI_RESET  + ". Select column (0-6):");
                    int col = getIntInput(0, 6);
                    while (!AiGame.dropDisc(col))
                    {
                        System.out.println("Invalid move. Try again.");
                        col = getIntInput(0, 6);
                    }
                    AiGame.printBoard();
                    if(AiGame.checkWin())
                    {
                        break;
                    }
                    AiGame.setCurrentPlayer();
                } while (!AiGame.checkWin() && !game.isBoardFull());

                if (AiGame.checkWin()) {
                    char winner = AiGame.getCurrentPlayerSymbol();
                    if(winner == AiGame.PLAYER1)
                    {
                        System.out.println(player1Name + " is the winner!");
                    }
                    else if(winner == AiGame.AI)
                    {
                        System.out.println("AI is the winner!");
                    }
                } else {
                    System.out.println("Tie!");
                }
            }
            // Play with Human
            else
            {

                System.out.println("Enter " + ANSI_TURQUOISE + "Player 1" + ANSI_RESET + " (" + ANSI_TURQUOISE + CIRCLE + ANSI_RESET + ") name:");
                String player1Name = scanner.next();

                String player2Name = getUniquesPlayersNames(player1Name, ANSI_PINK + "Player 2" + ANSI_RESET + "(" + ANSI_PINK + CIRCLE + ANSI_RESET + ")");

                printBoard();
                do {
                    game.setCurrentPlayer();
                    String currentPlayerColor = (game.getCurrentPlayerSymbol() == game.PLAYER1) ? ANSI_TURQUOISE : ANSI_PINK;
                    String currentPlayerName = (game.getCurrentPlayerSymbol() == game.PLAYER1) ? player1Name : player2Name;

                    System.out.println(currentPlayerColor + "Turn of: " + ANSI_RESET + currentPlayerName + ". Select column (0-6):");
                    int col = getIntInput(0, 6);
                    while (!game.dropDisc(col)) {
                        System.out.println("Invalid move. Try again.");
                        col = getIntInput(0, 6);
                    }
                    printBoard();
                } while (!game.checkWin() && !game.isBoardFull());

                if (game.checkWin()) {
                    char winner = game.getCurrentPlayerSymbol();
                    System.out.println((winner == game.PLAYER1 ? player1Name : player2Name) + " wins!");
                } else {
                    System.out.println("It's a draw!");
                }
            }
                stillPlaying = askToStillPlaying();

            }
            while (stillPlaying) ;

        }


    //ask still playing
    private boolean askToStillPlaying(){
        String input;
        do {
            System.out.println("Do you want to play again? (yes/no):");
            input = scanner.next().trim().toLowerCase();
            if (!input.equals("yes")&&!input.equals("no")) {
                System.out.println("Invalid response. Please select an option beetween 'yes' or 'no'.");
            }
        } while (!input.equals("yes")&&!input.equals("no"));
        return input.equals("yes");
    }

    //user input validations
    private int getIntInput(int min, int max){
        while (!scanner.hasNextInt()){
            System.out.println("Invalid input. Enter a number: ");
            scanner.next();
        }
        int number = scanner.nextInt();
        while (number < min || number > max) {
            System.out.println("Invalid input. Enter a number between "+ min+ " and "+ max+ ":");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number:");
                scanner.next();
            }
            number = scanner.nextInt();
            scanner.nextLine();
        }
        return number;
    }

    //to force different name for each player
    private String getUniquesPlayersNames(String player1, String player2) {
        System.out.println("Enter " + player2 + " name:");
        String name = scanner.next();

        while (name.equals(player1)) {
            System.out.println("The name cannot be the same. Enter a different name:");
            name = scanner.next();
        }

        return name;
    }

    //display board
    private void printBoard() {
        char[][] board = game.getBoard();

        System.out.println("-----------------------------");
        for (int i = 0; i < game.getRows(); i++) {
            System.out.print("|");

            for (int j = 0; j < game.getCols(); j++) {
                if (board[i][j] == game.PLAYER1) {
                    System.out.print(ANSI_TURQUOISE + "(" + CIRCLE + ")" + ANSI_RESET);
                } else if (board[i][j] == game.PLAYER2) {
                    System.out.print(ANSI_PINK + "(" + CIRCLE + ")" + ANSI_RESET);
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }

            System.out.println();

        }
        System.out.println("-----------------------------\n" +
                "  0   1   2   3   4   5   6 ");
    }


}
