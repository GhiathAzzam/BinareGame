public class State {
    private Cell[][] grid;
    private State parent;
    private int cost;

    public State(Cell[][] grid, State parent, int cost) {
        this.grid = copyGrid(grid);
        this.parent = parent;
        this.cost = cost;
    }

    public Cell[][] copyGrid(Cell[][] originalGrid) {
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


    // Deep copy of the grid to create independent states

    public Cell[][] getGrid() {
        return grid;
    }

    public State getParent() {
        return parent;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void incrementCost() {
        this.cost += 1;
    }
}
