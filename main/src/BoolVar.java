public class BoolVar {
    private int v;

    public BoolVar (int v) {
        if (v <= 0) {
            throw new IllegalArgumentException 
                ("Variable " + v + " must be positive");
        }
        this.v = v;
    }

    public int getV() {
        return v;
    }

    public String toString () {
        return Integer.toString(this.v);
    }
}
