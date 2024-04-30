package Arvore;

public class No {
    private String line;
    public No left;
    public No right;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public No(String line) {
        this.line = line;
        left = right = null;
    }
}
