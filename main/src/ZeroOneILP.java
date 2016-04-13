import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A Zero-One ILP problem is merely a list of constraints
 *
 * We also provide a (very naive) solution searcher that checks
 * each possibility for feasibility
 */
public class ZeroOneILP {
    private ArrayList<Constraint> constraints;

    public ZeroOneILP () {
        constraints = new ArrayList<Constraint> ();
    }

    public void addConstraint (Constraint c) {
        constraints.add(c);
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * @return whether the provided ILP solution is feasible 
     *         (satisfies all constraints)
     */
    public boolean isFeasible (ILPSolution soln) {
        boolean feasible = true;
        for (Constraint c : constraints) {
            feasible = feasible & c.isSatisfied(soln);
        }
        return feasible;
    }

    /**
     * @return a feasible ILP solution, if one exists (else null)
     */
    public ILPSolution findSoln () {
        ILPSolution soln = new ILPSolution ();

        /* first calculate all the unique variables mentioned */
        HashSet<ILPVariable> varset = new HashSet<ILPVariable>();
        for (Constraint c : constraints) {
            for (Term t : c.getTerms()) {
                varset.add (t.getVariable());
            }
        }
        ILPVariable [] vars = varset.toArray(new ILPVariable[0]);
        int numvars = vars.length;

        /* initial try at solution */
        for (ILPVariable zv : vars) {
            soln.add (zv, 0);
        }

        int index = 0;
        do {
            if (isFeasible(soln)) /* then done */
                return soln;

            /* else, next try at solution */
            for (index = 0; index < numvars; index++) {
                if (soln.getValue (vars[index]) == 0) {
                    soln.add (vars[index], 1);
                    break;
                } else {
                    soln.add (vars[index], 0);
                }
            }

            if (index == numvars) // reached end
                return null;
        } while (true);
    }
}
