import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class AiGame {
    private  final int ROWS = 6;
    private  final int COLS = 7;
    private  char[][] board;
    public final char PLAYER1 = 'P'; // Human Player
    public final char AI = 'A'; // Ai player
    public char currentPlayer;

    public HashMap<Character, Integer> scores;

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_TURQUOISE = "\u001B[36m";
    private final String ANSI_PINK = "\u001B[35m";
    private final String CIRCLE = "●";

    public AiGame(){
        board = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++)
            {
                board[i][j] = ' ';
            }
        }
        scores = new HashMap<>();
        scores.put('A', 1);
        scores.put('P', -1);
        scores.put('T', 0);
        scores.put(' ', 0);
        currentPlayer = AI;
    }
    public void setCurrentPlayer(){
        this.currentPlayer = (this.currentPlayer == PLAYER1) ? AI : PLAYER1;
    }


    public char getCurrentPlayerSymbol(){
        return currentPlayer;
    }

    public boolean dropDisc(int col) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (this.board[i][col] == ' ') {
                this.board[i][col] = this.currentPlayer;
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (this.board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin() {

        // Check horizontal
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j <= COLS - 4; j++) {
                if (isWinningSequence(i, j, 0, 1)) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int i = 0; i <= ROWS - 4; i++) {
            for (int j = 0; j < COLS; j++) {
                if (isWinningSequence(i, j, 1, 0)) {
                    return true;
                }
            }
        }

        // Check diagonals
        for (int i = 0; i <= ROWS - 4; i++) {
            for (int j = 0; j <= COLS - 4; j++) {
                if (isWinningSequence(i, j, 1, 1) || isWinningSequence(i, j + 3, 1, -1)) {
                    return true;
                }
            }
        }

        // Check for tie
        if (this.isBoardFull()) {
            return true;
        }

        return false;
    }

    private boolean isWinningSequence(int row, int col, int rowDelta, int colDelta) {
        char player = this.board[row][col];
        if (player == ' ') {
            return false;
        }

        for (int i = 1; i < 4; i++) {
            if (this.board[row + i * rowDelta][col + i * colDelta] != player) {
                return false;
            }
        }
        return true;
    }

    public List<int[]> validMove(char[][] currentState)
    {
        List<int[]> result = new ArrayList<>();
        int rows = ROWS - 1;
        for (int j = 0; j < COLS; j++)
        {
            if (result.size() >= 7) {
                // Break out of the loop if the result size reaches 8
                break;
            }
            if (currentState[rows][j] == ' ')
            {
                result.add(new int[]{rows, j});
            }
            else
            {
                int temp = rows;
                boolean done = true;
                while (done && temp >= 0)
                {
                    if (currentState[temp][j] == ' ')
                    {
                        result.add(new int[]{temp, j});
                        done = false;
                    }
                    temp--;
                }
            }
        }
        return result;
    }

    public int scorePosition(char[][] board, char token) {
        int score = 0;
        int COLUMN_COUNT = COLS;
        int ROW_COUNT = ROWS;
        int WINDOW_LENGTH = 4;

        // Score center column
        char[] centerArray = new char[ROW_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            centerArray[i] = board[i][COLUMN_COUNT / 2];
        }
        int centerCount = countOccurrences(centerArray, token);
        score += centerCount * 3;

        // Score Horizontal
        for (int r = 0; r < ROW_COUNT; r++) {
            for (int c = 0; c < COLUMN_COUNT - 3; c++) {
                char[] window = new char[WINDOW_LENGTH];
                System.arraycopy(board[r], c, window, 0, WINDOW_LENGTH);
                score += evaluateWindow(window, token);
            }
        }

        // Score Vertical
        for (int c = 0; c < COLUMN_COUNT; c++) {
            char[] colArray = new char[ROW_COUNT];
            for (int i = 0; i < ROW_COUNT; i++) {
                colArray[i] = board[i][c];
            }
            for (int r = 0; r < ROW_COUNT - 3; r++) {
                char[] window = new char[WINDOW_LENGTH];
                System.arraycopy(colArray, r, window, 0, WINDOW_LENGTH);
                score += evaluateWindow(window, token);
            }
        }

        // Score positive sloped diagonal
        for (int r = 0; r < ROW_COUNT - 3; r++) {
            for (int c = 0; c < COLUMN_COUNT - 3; c++) {
                char[] window = new char[WINDOW_LENGTH];
                for (int i = 0; i < WINDOW_LENGTH; i++) {
                    window[i] = board[r + i][c + i];
                }
                score += evaluateWindow(window, token);
            }
        }

        for (int r = 0; r < ROW_COUNT - 3; r++) {
            for (int c = 0; c < COLUMN_COUNT - 3; c++) {
                char[] window = new char[WINDOW_LENGTH];
                for (int i = 0; i < WINDOW_LENGTH; i++) {
                    window[i] = board[r + 3 - i][c + i];
                }
                score += evaluateWindow(window, token);
            }
        }
        return score;
    }

    private int countOccurrences(char[] array, char token) {
        int count = 0;
        for (int value : array) {
            if (value == token) {
                count++;
            }
        }
        return count;
    }

    private int evaluateWindow(char[] window, char token) {
        int score = 0;
        char oppToken = PLAYER1;
        if(token == PLAYER1)
        {
            oppToken = AI;
        }

        int tokenCount = countOccurrences(window, token);
        int emptyCount = countOccurrences(window, ' ');
        int oppTokenCount = countOccurrences(window, oppToken);

        if (tokenCount == 4) {
            score += 100;
        } else if (tokenCount == 3 && emptyCount == 1) {
            score += 5;
        } else if (tokenCount == 2 && emptyCount == 2) {
            score += 2;
        }

        if (oppTokenCount == 3 && emptyCount == 1) {
            score -= 4;
        }

        return score;
    }


    public void AiMove()
    {
        int bestMove = 0;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        List<int[]> locationCanMove = this.validMove(this.board);
        for(int[] position : locationCanMove)
        {
            this.board[position[0]][position[1]] = AI;
            int score = minimax(this.board, 8, alpha, beta, false);
            this.board[position[0]][position[1]] = ' ';
            if(score > bestScore)
            {
                bestScore = score;
                bestMove = position[1];
            }
        }
        this.dropDisc(bestMove);
    }

    public int minimax(char[][] currentState, int depth, int alpha, int beta, boolean isMaximizing)
    {
        if(checkWin() || depth == 0)
        {
            if(checkWin())
            {
                if(this.currentPlayer == AI)
                {
                    return Integer.MAX_VALUE;
                }
                else if (this.currentPlayer == PLAYER1)
                {
                    return Integer.MIN_VALUE;
                }
                else
                {
                    return 0;
                }
            }
            else if(depth == 0)
            {
                return scorePosition(currentState, currentPlayer);
            }
        }
        List<int[]> locationCanMove = this.validMove(currentState);
        if(isMaximizing)
        {
            int bestScore = Integer.MIN_VALUE;
            for(int[] position : locationCanMove)
            {
                currentState[position[0]][position[1]] = AI;
                int score = minimax(currentState, depth - 1, alpha, beta, false);
                currentState[position[0]][position[1]] = ' ';
                bestScore = Math.max(score, bestScore);
                alpha = Math.max(score, alpha);
                if(beta <= alpha)
                {
                    break;
                }
            }
            return bestScore;
        }
        else
        {
            int bestScore = Integer.MAX_VALUE;
            for(int[] position : locationCanMove)
            {
                currentState[position[0]][position[1]] = PLAYER1;
                int score = minimax(currentState, depth - 1, alpha, beta, true);
                currentState[position[0]][position[1]] = ' ';
                bestScore = Math.min(score, bestScore);
                beta = Math.min(score, beta);
                if(beta <= alpha)
                {
                    break;
                }
            }
            return bestScore;
        }
    }

    public char[][] getBoard() {
        return board;
    }
    public int getRows() {
        return ROWS;
    }
    public int getCols() {
        return COLS;
    }

    public void printBoard() {
        System.out.println("-----------------------------");
        for (int i = 0; i < ROWS; i++) {
            System.out.print("|");
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == PLAYER1) {
                    System.out.print(ANSI_TURQUOISE + "(" + CIRCLE + ")" + ANSI_RESET);
                } else if (board[i][j] == AI) {
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
