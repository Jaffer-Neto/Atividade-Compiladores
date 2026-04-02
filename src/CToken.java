
public class CToken {
    public final String name;
    public final String value;
    public final int line;
    public final int column;

    public CToken(String name, String value, int line, int column) {
        this.name = name;
        this.value = value;
        this.line = line + 1;
        this.column = column + 1;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', linha=%d, coluna=%d)",
                name, value, line, column);
    }
}