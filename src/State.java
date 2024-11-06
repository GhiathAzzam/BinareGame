public class State {
    private Cell[][] grid;
    private State parent;
    private int cost;

    public State(Cell[][] grid, State parent, int cost) {
        this.grid = grid;
        this.parent = parent;
        this.cost = cost;
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
