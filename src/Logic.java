import com.sun.source.tree.WhileLoopTree;

import java.util.*;

public class Logic {
    private Structure structure;
    private Scanner scanner = new Scanner(System.in);

    public Logic() {
    }

    Cell[][] initializeLevel1() {
        Cell[][] grid = new Cell[4][3];

        grid[0][0] = new Cell(CellType.LIGHT, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.LIGHT, null);
        grid[1][0] = new Cell(CellType.DARK, null);
        grid[1][1] = new Cell(CellType.FIXED, 3);
        grid[1][2] = new Cell(CellType.DARK, null);
        grid[2][0] = new Cell(CellType.DARK, null);
        grid[2][1] = new Cell(CellType.FIXED, 2);
        grid[2][2] = new Cell(CellType.DARK, null);
        grid[3][0] = new Cell(CellType.DARK, null);
        grid[3][1] = new Cell(CellType.LIGHT, null);
        grid[3][2] = new Cell(CellType.DARK, null);

        return grid;
    }

    Cell[][] initializeLevel2() {
        Cell[][] grid = new Cell[4][4];

        grid[0][0] = new Cell(CellType.LIGHT, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.DARK, null);
        grid[0][3] = new Cell(CellType.DARK, null);
        grid[1][0] = new Cell(CellType.DARK, null);
        grid[1][1] = new Cell(CellType.FIXED, 1);
        grid[1][2] = new Cell(CellType.FIXED, 2);
        grid[1][3] = new Cell(CellType.DARK, null);
        grid[2][0] = new Cell(CellType.DARK, null);
        grid[2][1] = new Cell(CellType.FIXED, 2);
        grid[2][2] = new Cell(CellType.FIXED, 1);
        grid[2][3] = new Cell(CellType.DARK, null);
        grid[3][0] = new Cell(CellType.DARK, null);
        grid[3][1] = new Cell(CellType.DARK, null);
        grid[3][2] = new Cell(CellType.DARK, null);
        grid[3][3] = new Cell(CellType.LIGHT, null);

        return grid;
    }

    Cell[][] initializeLevel3() {
        Cell[][] grid = new Cell[3][5];

        grid[0][0] = new Cell(CellType.DARK, null);
        grid[0][1] = new Cell(CellType.DARK, null);
        grid[0][2] = new Cell(CellType.DARK, null);
        grid[0][3] = null;
        grid[0][4] = null;
        grid[1][0] = new Cell(CellType.LIGHT, null);
        grid[1][1] = new Cell(CellType.FIXED, 2);
        grid[1][2] = new Cell(CellType.DARK, null);
        grid[1][3] = new Cell(CellType.FIXED, 3);
        grid[1][4] = new Cell(CellType.DARK, null);
        grid[2][0] = null;
        grid[2][1] = null;
        grid[2][2] = new Cell(CellType.DARK, null);
        grid[2][3] = new Cell(CellType.DARK, null);
        grid[2][4] = new Cell(CellType.DARK, null);

        return grid;
    }

    public Cell[][] initiateLevel() {
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

        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);
        while (true) {
            structure.printBoard();
//            testGetValidMoves();

            System.out.println("Enter row and column to select a LIGHT cell:");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != null) {
                if (structure.isMoveValid(x, y)) {
                    structure.applyMove(x, y);
                    if (structure.checkWin()) {
                        System.out.println("Congratulations! You have won the game.");
                        structure.printBoard();
                        break;
                    } else if (!structure.boardHasLightCells()) {
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

    public List<State> bfsHelper(State initialState, boolean forGUI) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(initialState);
        visited.add(stateToString(initialState));  // Convert state to string for unique identification

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            structure = new Structure(currentState);

            // Check for solution
            if (structure.checkWin()) {
                System.out.println("Solution found!");
                printSolutionPath(currentState);  // Print path for console
                if (forGUI) {
                    return constructSolutionPath(currentState);  // Return path for GUI
                } else {
                    return null;
                }
            }

            // Get next states
            List<State> nextStates = structure.getValidMoves();
            for (State nextState : nextStates) {
                String stateKey = stateToString(nextState);
                if (!visited.contains(stateKey)) {
                    queue.add(nextState);
                    visited.add(stateKey);
                }
            }
        }

        // No solution found
        System.out.println("No solution found.");
        return null;
    }

    public void solveBFS() {
        Cell[][] grid;
        grid = initiateLevel();

        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);
        bfsHelper(initialState, false);
    }

    public List<State> dfsHelper(State initialState, boolean forGUI) {
        Stack<State> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(initialState);
        visited.add(stateToString(initialState));

        while (!stack.isEmpty()) {
            State currentState = stack.pop();
            structure = new Structure(currentState);

            // Check if the current state is a winning state
            if (structure.checkWin()) {
                System.out.println("Solution found!");
                printSolutionPath(currentState);  // Print path for console
                if (forGUI) {
                    return constructSolutionPath(currentState);  // Return path for GUI
                } else {
                    return null;
                }
            }

            // Get all valid moves from the current state
            List<State> nextStates = structure.getValidMoves();
            for (State nextState : nextStates) {
                String stateKey = stateToString(nextState);
                if (!visited.contains(stateKey)) {
                    stack.push(nextState);
                    visited.add(stateKey);
                }
            }
        }

        // No solution found
        System.out.println("No solution found.");
        return null;
    }

    public void solveDFS() {
        Cell[][] grid = initiateLevel();

        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);

        dfsHelper(initialState, false);
    }

    public List<State> hillClimbingHelper(State initialState, boolean forGUI) {
        structure = new Structure(initialState);
        boolean solutionFound = false;

        while (!solutionFound) {
            structure.printBoard();

            // Check if the current state is a winning state
            if (structure.checkWin()) {
                if (forGUI) {
                    return constructSolutionPath(structure.getCurrentState());  // Return path for GUI
                } else {
                    System.out.println("Solution found!");
                    solutionFound = true;
                    break;
                }
            }

            // Get all valid moves from the current state
            List<State> nextStates = structure.getValidMoves();

            if (nextStates.isEmpty()) {
                System.out.println("No more moves available. Stuck in local optimum.");
                break;
            }

            // Choose the best move based on heuristic
            State bestState = selectBestState(nextStates);
            int currentHeuristic = calculateHeuristic(structure.getCurrentState());
            int bestStateHeuristic = calculateHeuristic(bestState);

            // If no better state is found, apply a random move
            if (bestStateHeuristic >= currentHeuristic) {
                System.out.println("No better moves found. Applying random move.");
                State randomState = nextStates.get(new Random().nextInt(nextStates.size()));
                structure.setCurrentState(randomState);
            } else {
                // Move to the best neighbor
                structure.setCurrentState(bestState);
            }
        }

        if (!solutionFound) {
            System.out.println("Solver couldn't find a solution (stuck in local optimum).");
        }
        return null;
    }

    public void solveHillClimbing() {
        Cell[][] grid = initiateLevel();

        State initialState = new State(grid, null, 0);
        structure = new Structure(initialState);

        hillClimbingHelper(initialState, false);
    }

    private State selectBestState(List<State> states) {
        State bestState = null;
        int bestHeuristic = Integer.MAX_VALUE;

        for (State state : states) {
            int heuristicValue = calculateHeuristic(state);
            if (heuristicValue < bestHeuristic) {
                bestHeuristic = heuristicValue;
                bestState = state;
            }
        }
        return bestState;
    }

    private int calculateHeuristic(State state) {
        Cell[][] grid = state.getGrid();
        int heuristic = 0;

        // Heuristic: Sum of differences between required and actual lights around each FIXED cell
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null && grid[i][j].getType() == CellType.FIXED) {
                    int requiredLights = grid[i][j].getFixedValue();
                    int actualLights = structure.countAdjacentLights(i, j, grid);
                    heuristic += Math.abs(requiredLights - actualLights);
                }
            }
        }
        return heuristic;
    }

    private List<State> constructSolutionPath(State goalState) {
        List<State> path = new ArrayList<>();
        State current = goalState;

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        Collections.reverse(path);
        return path;
    }

    private void printSolutionPath(State goalState) {
        List<State> path = constructSolutionPath(goalState);
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

    public void testGetValidMoves() {
        System.out.println("Initial Board:");
        structure.printBoard();

        List<State> validMoves = structure.getValidMoves();

        System.out.println("\nNumber of Valid Moves: " + validMoves.size());

        for (int i = 0; i < validMoves.size(); i++) {
            System.out.println("\nValid Move " + (i + 1) + ":");
            Structure tempStructure = new Structure(validMoves.get(i));
            tempStructure.printBoard();
        }
    }

    public boolean areStatesEqual(State state1, State state2) {
        if (state1.getCost() != state2.getCost()) {
            return false;
        }

        Cell[][] grid1 = state1.getGrid();
        Cell[][] grid2 = state2.getGrid();

        if (grid1.length != grid2.length || grid1[0].length != grid2[0].length) {
            return false;
        }

        for (int i = 0; i < grid1.length; i++) {
            for (int j = 0; j < grid1[i].length; j++) {
                Cell cell1 = grid1[i][j];
                Cell cell2 = grid2[i][j];

                if (cell1 == null && cell2 != null || cell1 != null && cell2 == null) {
                    return false;
                }

                if (cell1 != null && cell2 != null) {
                    if (cell1.getType() != cell2.getType() ||
                            (cell1.getFixedValue() != null && !cell1.getFixedValue().equals(cell2.getFixedValue()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
