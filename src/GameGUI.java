import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameGUI extends JFrame {
    private Structure structure;
    private JButton[][] cellButtons;
    private Logic logic;
    private int rows;
    private int cols;
    private JPanel boardPanel;

    public GameGUI() {
        logic = new Logic();
        setupGUI();
        setupInitialOptions();
    }

    private void setupInitialOptions() {
        String[] levels = {"Level 1", "Level 2", "Level 3"};
        int selectedLevel = JOptionPane.showOptionDialog(
                this,
                "Select a level to play:",
                "Choose Level",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                levels,
                levels[0]
        );

        Cell[][] initialGrid;
        switch (selectedLevel) {
            case 1:
                initialGrid = logic.initializeLevel2();
                break;
            case 2:
                initialGrid = logic.initializeLevel3();
                break;
            default:
                initialGrid = logic.initializeLevel1();
                break;
        }

        rows = initialGrid.length;
        cols = initialGrid[0].length;
        State initialState = new State(initialGrid, null, 0);
        structure = new Structure(initialState);
        setupBoard();
    }

    private void setupGUI() {
        setTitle("Binary Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());

        String[] solveOptions = {"Solve with BFS", "Solve with DFS"};
        JComboBox<String> solveComboBox = new JComboBox<>(solveOptions);
        solveComboBox.addActionListener(e -> solveGame(solveComboBox.getSelectedItem().toString()));

        controlPanel.add(resetButton);
        controlPanel.add(solveComboBox);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setupBoard() {
        if (boardPanel != null) {
            remove(boardPanel);
        }

        boardPanel = new JPanel(new GridLayout(rows, cols));
        cellButtons = new JButton[rows][cols];

        Cell[][] grid = structure.getCurrentState().getGrid();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellButtons[i][j] = new JButton();
                updateButtonAppearance(cellButtons[i][j], grid[i][j]);
                final int x = i, y = j;

                cellButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(x, y);
                    }
                });

                boardPanel.add(cellButtons[i][j]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void updateButtonAppearance(JButton button, Cell cell) {
        if (cell == null) {
            button.setBackground(new Color(220, 220, 220));
            button.setEnabled(false);
        } else if (cell.getType() == CellType.LIGHT) {
            button.setBackground(new Color(173, 216, 230));
            button.setText("L");
        } else if (cell.getType() == CellType.DARK) {
            button.setBackground(new Color(105, 105, 105));
            button.setText("D");
        } else if (cell.getType() == CellType.FIXED) {
            button.setBackground(new Color(11, 10, 10));
            button.setText(cell.getFixedValue() != null ? String.valueOf(cell.getFixedValue()) : "F");
            button.setEnabled(false);
        }
    }

    private void handleCellClick(int x, int y) {
        if (structure.isMoveValid(x, y)) {
            structure.applyMove(x, y);
            updateBoard();

            if (structure.checkWin()) {
                JOptionPane.showMessageDialog(this, "Congratulations! You've won the game!");
                resetGame();
            } else if (!structure.boardHasLightCells()) {
                JOptionPane.showMessageDialog(this, "Game Over - No more moves!");
                resetGame();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move. Try another cell.");
        }
    }

    private void updateBoard() {
        Cell[][] grid = structure.getCurrentState().getGrid();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                updateButtonAppearance(cellButtons[i][j], grid[i][j]);
            }
        }
    }

    private void resetGame() {
        setupInitialOptions();
        setupBoard();
    }

    private void solveGame(String selectedOption) {
        State initialState = structure.getCurrentState();
        List<State> solutionPath;

        if (selectedOption.equals("Solve with BFS")) {
            solutionPath = logic.BFS(initialState);
        } else {
            solutionPath = logic.DFS(initialState);
        }

        if (solutionPath != null) {
            new Timer(500, new ActionListener() {
                int step = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (step < solutionPath.size()) {
                        structure = new Structure(solutionPath.get(step));
                        updateBoard();
                        step++;
                    } else {
                        ((Timer) e.getSource()).stop();
                    }
                }
            }).start();
        } else {
            JOptionPane.showMessageDialog(this, "No solution found.");
        }
    }
}
