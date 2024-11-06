import java.util.*;

public class Structure {
    private State currentState;

    public Structure(State initialState) {
        this.currentState = initialState;
    }

    public boolean isMoveValid(int x, int y) {
        // Check if the cell is within bounds, not null, and is a LIGHT cell
        Cell[][] grid = currentState.getGrid();
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != null && grid[x][y].getType() == CellType.LIGHT;
    }

    public void applyMove(int x, int y) {
        // Convert chosen cell from LIGHT to DARK and toggle adjacent cells
        if (isMoveValid(x, y)) {
            Cell[][] newGrid = copyGrid(currentState.getGrid());  // to ensure the state has independent copy of the parent grid unaffected by changes in other states
            newGrid[x][y].setType(CellType.DARK);
            toggleAdjacentCells(x, y, newGrid);

            // Create the new state, linking it to the current state
            State newState = new State(newGrid, currentState, currentState.getCost() + 1);
            currentState = newState;
//            currentState.incrementCost();
        }
    }

    private Cell[][] copyGrid(Cell[][] originalGrid) {
        Cell[][] newGrid = new Cell[originalGrid.length][originalGrid[0].length];
        for (int i = 0; i < originalGrid.length; i++) {
            for (int j = 0; j < originalGrid[i].length; j++) {
                if (originalGrid[i][j] != null) {
                    CellType type = originalGrid[i][j].getType();
                    Integer fixedValue = originalGrid[i][j].getFixedValue();
                    newGrid[i][j] = new Cell(type, fixedValue);
                }
            }
        }
        return newGrid;
    }

    private void toggleAdjacentCells(int x, int y, Cell[][] grid) {
        // Toggle the adjacent cells (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length && grid[nx][ny] != null && grid[nx][ny].getType() != CellType.FIXED) {
                grid[nx][ny].setType(grid[nx][ny].getType() == CellType.LIGHT ? CellType.DARK : CellType.LIGHT);
            }
        }
    }

    public boolean checkWin() {
        // Check if each FIXED cell is surrounded by the specified number of LIGHT cells
        Cell[][] grid = currentState.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null && grid[i][j].getType() == CellType.FIXED) {
                    int requiredLights = grid[i][j].getFixedValue();
                    int surroundingLights = countAdjacentLights(i, j, grid);
                    if (surroundingLights != requiredLights) return false;
                }
            }
        }
        return true;
    }

    private int countAdjacentLights(int x, int y, Cell[][] grid) {
        // Count the LIGHT cells adjacent to (x, y)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int lightCount = 0;
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length && grid[nx][ny] != null && grid[nx][ny].getType() == CellType.LIGHT) {
                lightCount++;
            }
        }
        return lightCount;
    }

    public void printBoard() {
        Cell[][] grid = currentState.getGrid();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell == null) {
                    System.out.print("  ");
                } else if (cell.getType() == CellType.LIGHT) {
                    System.out.print("L ");
                } else if (cell.getType() == CellType.DARK) {
                    System.out.print("D ");
                } else if (cell.getType() == CellType.FIXED) {
                    System.out.print(cell.getFixedValue() + " ");
                }
            }
            System.out.println();
        }
        System.out.println("Current Cost: " + currentState.getCost());
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean boardHasLightCells() {
        // Check if each FIXED cell is surrounded by the specified number of LIGHT cells
        Cell[][] grid = currentState.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null && grid[i][j].getType() == CellType.LIGHT) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<State> getValidMoves() {
        List<State> nextStates = new ArrayList<>();
        Cell[][] grid = currentState.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // Check if the current cell is a valid move
                if (isMoveValid(i, j)) {
                    // Create a new state for this move
                    Cell[][] newGrid = copyGrid(grid);
                    newGrid[i][j].setType(CellType.DARK);
                    toggleAdjacentCells(i, j, newGrid);

                    // Create a new state with the new grid and add it to the list
                    State newState = new State(newGrid, currentState, currentState.getCost() + 1);
                    nextStates.add(newState);
                }
            }
        }
        return nextStates;
    }

}

