public class Cell {
    private CellType type;
    private Integer fixedValue; // Only for fixed cells, otherwise null

    public Cell(CellType type, Integer fixedValue) {
        this.type = type;
        this.fixedValue = fixedValue;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setFixedValue(Integer fixedValue) {
        this.fixedValue = fixedValue;
    }

    public CellType getType() {
        return type;
    }

    public Integer getFixedValue() {
        return fixedValue;
    }
// Getter and Setter methods for `type` and `fixedValue`
}
