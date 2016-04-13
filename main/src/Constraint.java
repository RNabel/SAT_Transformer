import java.util.List;

public class Constraint {
    public enum Op { LE, GE, EQ };

    private List<Term> terms;
    private int bound;
    private Op op;

    public Constraint (List<Term> terms, Op op, int bound) {
        this.terms = terms;
        this.op = op;
        this.bound = bound;
    }

    public List<Term> getTerms () {
        return terms;
    }

    public Op getOp () {
        return op;
    }

    public int getBound () {
        return bound;
    }

    public boolean isSatisfied (ILPSolution soln) {
        int lhs = 0;
        for (Term t: terms) {
            lhs += t.termValue(soln);
        }
        if (op == Op.EQ) {
            return (lhs == bound);
        } else if (op == Op.LE) {
            return (lhs <= bound);
        } else {
            return (lhs >= bound);
        }
    }

    public String toString() {
        String sop; 
        if (op == Op.EQ) {
            sop = " = ";
        } else if (op == Op.LE) {
            sop = " <= ";
        } else {
            sop = " >= ";
        }
        StringBuilder tsb = new StringBuilder();
        boolean hasprev = false;
        for (Term t : terms) {
            if (hasprev) {
                tsb.append(" + ");
            }
            hasprev = true;
            tsb.append (t);
        }
        return tsb.toString() + sop + bound;
    }
}
