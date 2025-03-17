public class Game {
    private  final int ROWS = 6;
    private  final int COLS = 7;
    private  char[][] board;
    public final char PLAYER1 = 'T'; // Turquoise player
    public final char PLAYER2 = 'P'; // Pink player
    public char currentPlayer;

    public Game(){
        char[][] board = new char[ROWS][COLS];
        for (int i =0;i<board.length;i++){
            for (int j = 0; j<board[i].length;j++){
                board[i][j] =' ';
            }
        }
        this.board = board;
    }
    public void setCurrentPlayer(){
        this.currentPlayer = (this.currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
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
                if (this.board[i][j] ==' ') {
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
                if (this.board[i][j] == this.currentPlayer &&
                        this.board[i][j+1] == this.currentPlayer &&
                        this.board[i][j+2] == this.currentPlayer &&
                        this.board[i][j+3] == this.currentPlayer) {
                    return true;
                }
            }
        }
        // Check vertical
        for (int i = 0; i <= ROWS - 4; i++) {
            for (int j = 0; j < COLS; j++) {
                if (this.board[i][j] == this.currentPlayer &&
                        this.board[i+1][j] == this.currentPlayer &&
                        this.board[i+2][j] == this.currentPlayer &&
                        this.board[i+3][j] == this.currentPlayer) {
                    return true;
                }
            }
        }
        // Check diagonals
        for (int i = 0; i <= ROWS - 4; i++) {
            for (int j = 0; j <= COLS - 4; j++) {
                if (this.board[i][j] == this.currentPlayer &&
                        this.board[i+1][j+1] == this.currentPlayer &&
                        this.board[i+2][j+2] == this.currentPlayer &&
                        this.board[i+3][j+3] == this.currentPlayer) {
                    return true;
                }
                if (this.board[i][j+3] == this.currentPlayer &&
                        this.board[i+1][j+2] == this.currentPlayer &&
                        this.board[i+2][j+1] == this.currentPlayer &&
                        this.board[i+3][j] == this.currentPlayer) {
                    return true;
                }
            }
        }
        return false;
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
}
