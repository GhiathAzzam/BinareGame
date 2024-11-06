import java.util.*;

public class Logic {
    private Structure structure;
    private Scanner scanner = new Scanner(System.in);

    public Logic() {
        // Empty constructor - the structure will be initialized in userPlay
    }

    public Cell[][] initiateLevel(){
        System.out.println("Choose a level (1, 2, or 3): ");
        int level = scanner.nextInt();

        switch (level) {
            case 1:
                return initializeLevel1();
            case 2:
                return initializeLevel2();
            case 3:
                return initializeLevel3();
            default:
                System.out.println("Invalid level. Starting with level 1.");
                return initializeLevel1();
        }
    }

    public void userPlay() {
        Cell[][] grid;
        grid = initiateLevel();

        // Initialize the structure with the selected level's grid
        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);
        // Start the game loop
        while (true) {
            structure.printBoard();
//            testGetValidMoves();

            System.out.println("Enter row and column to select a LIGHT cell:");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            // Ensure selected cell is within bounds and not null
            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != null) {
                if (structure.isMoveValid(x, y)) {
                    structure.applyMove(x, y);
                    if (structure.checkWin()) {
                        System.out.println("Congratulations! You have won the game.");
                        structure.printBoard();
                        break;
                    }
                    else if (!structure.boardHasLightCells()){
                        System.out.println("Game Over...");
                        structure.printBoard();
                        break;
                    }
                } else {
                    System.out.println("Invalid move! Try again.");
                }
            } else {
                System.out.println("Invalid cell! Choose a valid LIGHT cell.");
            }
        }
    }

    public void solveBFS() {
        Cell[][] grid;
        grid = initiateLevel();

        // Initialize the structure with the selected level's grid
        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);

        // Call BFS to solve the game
        BFS(initialState);
    }

    private Cell[][] initializeLevel1() {
        Cell[][] grid = new Cell[4][3];

        grid[0][0] = new Cell(CellType.LIGHT, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.LIGHT, null);
        grid[1][0] = new Cell(CellType.DARK, null);
        grid[1][1] = new Cell(CellType.FIXED, 3);  // Fixed cell with required 3 light cells
        grid[1][2] = new Cell(CellType.DARK, null);
        grid[2][0] = new Cell(CellType.DARK, null);
        grid[2][1] = new Cell(CellType.FIXED, 2);  // Fixed cell with required 2 light cells
        grid[2][2] = new Cell(CellType.DARK, null);
        grid[3][0] = new Cell(CellType.DARK, null);
        grid[3][1] = new Cell(CellType.LIGHT, null);
        grid[3][2] = new Cell(CellType.DARK, null);

        return grid;
    }

    private Cell[][] initializeLevel2() {
        Cell[][] grid = new Cell[4][4];

        grid[0][0] = new Cell(CellType.LIGHT, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.DARK, null);
        grid[0][3] = new Cell(CellType.DARK, null);
        grid[1][0] = new Cell(CellType.DARK, null);
        grid[1][1] = new Cell(CellType.FIXED, 1);  // Fixed cell with required 1 light cell
        grid[1][2] = new Cell(CellType.FIXED, 2);  // Fixed cell with required 2 light cells
        grid[1][3] = new Cell(CellType.DARK, null);
        grid[2][0] = new Cell(CellType.DARK, null);
        grid[2][1] = new Cell(CellType.FIXED, 2);  // Fixed cell with required 2 light cells
        grid[2][2] = new Cell(CellType.FIXED, 1);  // Fixed cell with required 1 light cell
        grid[2][3] = new Cell(CellType.DARK, null);
        grid[3][0] = new Cell(CellType.DARK, null);
        grid[3][1] = new Cell(CellType.DARK, null);
        grid[3][2] = new Cell(CellType.DARK, null);
        grid[3][3] = new Cell(CellType.LIGHT, null);

        return grid;
    }

    private Cell[][] initializeLevel3() {
        Cell[][] grid = new Cell[3][5];

        grid[0][0] = new Cell(CellType.DARK, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.DARK, null);
        grid[0][3] = null; // Represents a null or empty cell
        grid[0][4] = null; // Represents a null or empty cell
        grid[1][0] = new Cell(CellType.LIGHT, null);
        grid[1][1] = new Cell(CellType.FIXED, 2);  // Fixed cell with required 2 light cells
        grid[1][2] = new Cell(CellType.DARK, null);
        grid[1][3] = new Cell(CellType.FIXED, 3);  // Fixed cell with required 3 light cells
        grid[1][4] = new Cell(CellType.DARK, null);
        grid[2][0] = null; // Represents a null or empty cell
        grid[2][1] = null; // Represents a null or empty cell
        grid[2][2] = new Cell(CellType.DARK, null);
        grid[2][3] = new Cell(CellType.DARK, null);
        grid[2][4] = new Cell(CellType.DARK, null);

        return grid;
    }
    public void testGetValidMoves() {
        System.out.println("Initial Board:");
        structure.printBoard();  // Print the initial state

        List<State> validMoves = structure.getValidMoves();

        System.out.println("\nNumber of Valid Moves: " + validMoves.size());

        // Iterate through each valid move state and print the board
        for (int i = 0; i < validMoves.size(); i++) {
            System.out.println("\nValid Move " + (i + 1) + ":");
            Structure tempStructure = new Structure(validMoves.get(i));
            tempStructure.printBoard();
        }
    }

    public boolean areStatesEqual(State state1, State state2) {
        // Check if the costs are different
        if (state1.getCost() != state2.getCost()) {
            return false;
        }

        Cell[][] grid1 = state1.getGrid();
        Cell[][] grid2 = state2.getGrid();

        // Check if the grid dimensions are different
        if (grid1.length != grid2.length || grid1[0].length != grid2[0].length) {
            return false;
        }

        // Compare each cell in the two grids
        for (int i = 0; i < grid1.length; i++) {
            for (int j = 0; j < grid1[i].length; j++) {
                Cell cell1 = grid1[i][j];
                Cell cell2 = grid2[i][j];

                // If one cell is null and the other is not, they are not equal
                if (cell1 == null && cell2 != null || cell1 != null && cell2 == null) {
                    return false;
                }

                // If both cells are not null, compare their type and fixed value
                if (cell1 != null && cell2 != null) {
                    if (cell1.getType() != cell2.getType() ||
                            (cell1.getFixedValue() != null && !cell1.getFixedValue().equals(cell2.getFixedValue()))) {
                        return false;
                    }
                }
            }
        }
        // If all checks pass, the states are equal
        return true;
    }

    public void BFS(State initialState) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<State, State> parentMap = new HashMap<>();

        queue.add(initialState);
        visited.add(stateToString(initialState));  // Convert state to string for unique identification

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            Structure structure = new Structure(currentState);

            // Check if the current state is the goal (win condition)
            if (structure.checkWin()) {
                System.out.println("Solution found!");
                printSolutionPath(currentState, parentMap);
                return;
            }

            // Generate all valid moves from the current state
            List<State> nextStates = structure.getValidMoves();

            for (State nextState : nextStates) {
                String stateKey = stateToString(nextState);

                // Only process if the state hasn't been visited
                if (!visited.contains(stateKey)) {
                    queue.add(nextState);
                    visited.add(stateKey);
                    parentMap.put(nextState, currentState);  // Track the parent for solution path
                }
            }
        }

        System.out.println("No solution found.");
    }

    private void printSolutionPath(State goalState, Map<State, State> parentMap) {
        List<State> path = new ArrayList<>();
        State current = goalState;

        // Trace back from the goal state to the initial state
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);  // Reverse to get the path from start to goal

        for (State state : path) {
            Structure tempStructure = new Structure(state);
            tempStructure.printBoard();
            System.out.println("Cost: " + state.getCost());
            System.out.println("-----");
        }
    }

    private String stateToString(State state) {
        StringBuilder sb = new StringBuilder();
        Cell[][] grid = state.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell == null) {
                    sb.append("null,");
                } else if (cell.getType() == CellType.LIGHT) {
                    sb.append("L,");
                } else if (cell.getType() == CellType.DARK) {
                    sb.append("D,");
                } else if (cell.getType() == CellType.FIXED) {
                    sb.append("F").append(cell.getFixedValue()).append(",");
                }
            }
        }
        return sb.toString();
    }

}
