/** 
 * Terms are an integer coefficient times a ILPvariable
 */
public class Term {
    private int coefficient;
    private ILPVariable zv;

    public Term (int coeff, ILPVariable zv) {
        this.coefficient = coeff;
        this.zv = zv;
    }

    public int getCoefficient () {
        return coefficient;
    }

    public ILPVariable getVariable () {
        return zv;
    }

    public String toString () {
        return Integer.toString (coefficient) + "*" + zv;
    }

    /**
     * @return value of this term under the given solution
     */
    public int termValue (ILPSolution soln) {
        return coefficient * soln.getValue(zv);
    }
}
