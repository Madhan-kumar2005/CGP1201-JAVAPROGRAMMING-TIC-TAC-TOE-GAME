import java.awt.*;
import java.awt.event.*;

public class CreativeTicTacToe extends Frame implements ActionListener {
    private Button[] buttons = new Button[9];
    private boolean isXturn = true; // Track turns (true = X's turn, false = O's turn)
    private String winner = null; // To track the winner
    private Label winnerLabel = new Label(""); // For displaying winner message
    private Button resetButton = new Button("Reset Game");
    private Label xLabel = new Label("X is You");
    private Label oLabel = new Label("O is Machine");

    // Score tracking
    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;
    private Label xWinsLabel = new Label("X Wins: 0");
    private Label oWinsLabel = new Label("O Wins: 0");
    private Label drawsLabel = new Label("Draws: 0");

    public CreativeTicTacToe() {
        // Frame settings
        setTitle("Creative Tic-Tac-Toe");
        setSize(800, 750); // Increased width for score table
        setLayout(null);
        setBackground(new Color(255, 235, 180)); // Warm background color

        // Create a panel for the grid (3x3 game board)
        Panel gridPanel = new Panel();
        gridPanel.setLayout(new GridLayout(3, 3));
        gridPanel.setBounds(150, 200, 300, 300); // Grid size fixed, but position will adjust dynamically
        gridPanel.setBackground(new Color(240, 240, 240));

        // Create and add buttons to the grid
        for (int i = 0; i < 9; i++) {
            buttons[i] = new Button("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].setBackground(Color.LIGHT_GRAY);
            buttons[i].addActionListener(this);
            gridPanel.add(buttons[i]);
        }

        // Add grid panel to the frame
        add(gridPanel);

        // Winner label (for displaying X or O wins message)
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        winnerLabel.setBounds(200, 160, 200, 30);
        winnerLabel.setAlignment(Label.CENTER);
        winnerLabel.setBackground(new Color(255, 235, 180));
        add(winnerLabel);

        // X label
        xLabel.setFont(new Font("Arial", Font.BOLD, 18));
        xLabel.setBounds(200, 550, 200, 30);
        xLabel.setAlignment(Label.CENTER);
        xLabel.setBackground(new Color(255, 200, 150));
        add(xLabel);

        // O label
        oLabel.setFont(new Font("Arial", Font.BOLD, 18));
        oLabel.setBounds(200, 590, 200, 30);
        oLabel.setAlignment(Label.CENTER);
        oLabel.setBackground(new Color(255, 200, 150));
        add(oLabel);

        // Reset button (to reset the game)
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBounds(240, 630, 120, 30);
        resetButton.setBackground(Color.GREEN); // Reset button color changed to green
        resetButton.addActionListener(e -> resetGame());
        add(resetButton);

        // Scoreboard labels
        xWinsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        xWinsLabel.setBounds(480, 200, 150, 30);
        xWinsLabel.setAlignment(Label.LEFT);
        xWinsLabel.setBackground(new Color(220, 220, 220));
        add(xWinsLabel);

        oWinsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        oWinsLabel.setBounds(480, 250, 150, 30);
        oWinsLabel.setAlignment(Label.LEFT);
        oWinsLabel.setBackground(new Color(220, 220, 220));
        add(oWinsLabel);

        drawsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        drawsLabel.setBounds(480, 300, 150, 30);
        drawsLabel.setAlignment(Label.LEFT);
        drawsLabel.setBackground(new Color(220, 220, 220));
        add(drawsLabel);

        // Close button behavior
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();

        // Check if the button is already clicked
        if (!clickedButton.getLabel().equals("") || winner != null) {
            return;
        }

        // User's turn (X)
        if (isXturn) {
            clickedButton.setLabel("X");
            clickedButton.setBackground(Color.CYAN);

            // Check for win or draw
            if (checkWin()) {
                winner = "X";
                xWins++;
                xWinsLabel.setText("X Wins: " + xWins);
                winnerLabel.setText("Player X Wins!");
                winnerLabel.setForeground(Color.BLUE);
                return;
            } else if (isDraw()) {
                draws++;
                drawsLabel.setText("Draws: " + draws);
                winnerLabel.setText("It's a Draw!");
                winnerLabel.setForeground(Color.RED);
                return;
            }

            // Switch to "O" turn
            isXturn = false;

            // Call automatic "O" placement
            placeO();
        }
    }

    private void placeO() {
        // Priority 1: Place in the center if available
        if (buttons[4].getLabel().equals("")) {
            placeOAt(4);
            return;
        }

        // Priority 2: Place in a corner if available (top-left, top-right, bottom-left, bottom-right)
        int[] corners = {0, 2, 6, 8};
        for (int corner : corners) {
            if (buttons[corner].getLabel().equals("")) {
                placeOAt(corner);
                return;
            }
        }

        // Priority 3: Place in any remaining spot
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getLabel().equals("")) {
                placeOAt(i);
                return;
            }
        }
    }

    private void placeOAt(int index) {
        buttons[index].setLabel("O");
        buttons[index].setBackground(Color.PINK);

        // Check for win or draw
        if (checkWin()) {
            winner = "O";
            oWins++;
            oWinsLabel.setText("O Wins: " + oWins);
            winnerLabel.setText("Player O Wins!");
            winnerLabel.setForeground(Color.BLUE);
        } else if (isDraw()) {
            draws++;
            drawsLabel.setText("Draws: " + draws);
            winnerLabel.setText("It's a Draw!");
            winnerLabel.setForeground(Color.RED);
        } else {
            isXturn = true;
        }
    }

    private boolean checkWin() {
        // Winning combinations
        int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] pos : winPositions) {
            if (!buttons[pos[0]].getLabel().equals("") &&
                buttons[pos[0]].getLabel().equals(buttons[pos[1]].getLabel()) &&
                buttons[pos[0]].getLabel().equals(buttons[pos[2]].getLabel())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDraw() {
        for (Button button : buttons) {
            if (button.getLabel().equals("")) {
                return false; // Empty button means game is not a draw
            }
        }
        return true; // No empty buttons, it's a draw
    }

    private void resetGame() {
        for (Button button : buttons) {
            button.setLabel("");
            button.setBackground(Color.LIGHT_GRAY);
            button.setEnabled(true);
        }
        winner = null;
        isXturn = true;
        winnerLabel.setText("");
    }

    public static void main(String[] args) {
        new CreativeTicTacToe();
    }
}