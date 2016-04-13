public class Literal {
    private BoolVar v;
    private boolean sign;

    public Literal (int l) {
        if (l == 0) {
            throw new IllegalArgumentException ("0 is not a valid literal");
        }
        if (l > 0) { 
            v = new BoolVar(l); 
            sign = true;
        } else { 
            v = new BoolVar(-l);
            sign = false;
        }
    }

    public BoolVar getVar () {
        return v;
    }

    public boolean getSign () {
        return sign;
    }

    public String toString () {
        String s = sign ? "+" : "-";
        String vs = v.toString();
        return s + vs;
    }
}
