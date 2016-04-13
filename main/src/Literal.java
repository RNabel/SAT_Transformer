public class Literal {
    private BoolVar v;
    private boolean isPositive;

    public Literal (int l) {
        if (l == 0) {
            throw new IllegalArgumentException ("0 is not a valid literal");
        }
        if (l > 0) { 
            v = new BoolVar(l); 
            isPositive = true;
        } else { 
            v = new BoolVar(-l);
            isPositive = false;
        }
    }

    public BoolVar getVar () {
        return v;
    }

    public boolean getIsPositive() {
        return isPositive;
    }

    public String toString () {
        String s = isPositive ? "+" : "-";
        String vs = v.toString();
        return s + vs;
    }
}
