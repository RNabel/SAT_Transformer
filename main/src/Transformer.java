import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Class transforming NPC problems between each other.
 */
public class Transformer {
    /**
     * Class that keeps track of literal indices. Allows easy passing back and forth of indices.
     */
    static class Tracker {
        public int currentOffset;
    }

    /**
     * Converts an SAT to 3-SAT.
     *
     * @param inputSAT The SAT to be changed.
     * @return The 3-SAT
     */
    public static SATinstance satToThreeSat(SATinstance inputSAT) {
        SATinstance outputSAT = new SATinstance();

        int maxIndex = 0;

        for (Clause clause : inputSAT.clauses)
            for (Iterator<Literal> it = clause.getLiterals(); it.hasNext(); ) {
                Literal currentLiteral = it.next();
                int currentIndex = currentLiteral.getVar().getV();
                maxIndex = (maxIndex > currentIndex) ? maxIndex : currentIndex;
            }

        // Set up the dummy index tracker.
        Tracker offsetTracker = new Transformer.Tracker();
        offsetTracker.currentOffset = maxIndex;

        // Loop through each clause and break up as necessary.
        for (Clause clause : inputSAT.clauses) {
            if (clause.getLength() > 3) {
                // Split up clause.
                List<Clause> clauses = Transformer.splitClause(clause, offsetTracker);
                clauses.forEach(outputSAT::addClause); // Add each clause to the resulting SAT.
            } else {
                outputSAT.addClause(clause);
            }
        }

        return outputSAT;
    }

    /**
     * Splits a clause into subsections of 3.
     *
     * @param clause        The clause to split.
     * @param offsetTracker The index of the last-used variable.
     * @return Array of clauses
     */
    private static List<Clause> splitClause(Clause clause, Tracker offsetTracker) {
        int length = clause.getLength();
        int dummyOffset = offsetTracker.currentOffset;

        List<Clause> clauses = new ArrayList<>();
        ListIterator<Literal> literals = clause.getLiterals();

        // Initialize dummy variable.
        Literal currentDummy = new Literal(++dummyOffset);

        // Create first clause.
        Clause currentClause = new Clause();
        currentClause.addLiteral(literals.next());
        currentClause.addLiteral(literals.next());
        currentClause.addLiteral(currentDummy);
        clauses.add(currentClause);

        // Create middle clauses.
        for (int i = 2; i < length - 2; i++) {
            currentClause = new Clause();

            // Add negated dummy.
            currentDummy = new Literal(-dummyOffset);
            currentClause.addLiteral(currentDummy);

            // Add currentLiteral.
            Literal currentLiteral = literals.next();
            currentClause.addLiteral(currentLiteral);

            // Add new dummy.
            currentDummy = new Literal(++dummyOffset);
            currentClause.addLiteral(currentDummy);

            clauses.add(currentClause);
        }

        // Create last clause.
        currentClause = new Clause();
        currentDummy = new Literal(-dummyOffset);
        currentClause.addLiteral(currentDummy);
        currentClause.addLiteral(literals.next());
        currentClause.addLiteral(literals.next());
        clauses.add(currentClause);

        // Update the offsetTracker before returning.
        offsetTracker.currentOffset = dummyOffset;

        return clauses;
    }

    /**
     * Convert SAT to 0-1 ILP.
     *
     * @param inputSAT The SAT to convert.
     * @return The 0-1 ILP object.
     */
    public static ZeroOneILP satToILP(SATinstance inputSAT) {
        ZeroOneILP outputILP = new ZeroOneILP();

        // Loop through each clause.
        for (Clause clause : inputSAT.clauses) {
            int numOfNegativeTerms = 0;

            List<Term> terms = new ArrayList<>();

            // Loop through all literals.=
            for (Iterator<Literal> it = clause.getLiterals(); it.hasNext(); ) {
                Literal literal = it.next();

                int coeff = literal.getIsPositive() ? 1 : -1;
                numOfNegativeTerms += literal.getIsPositive() ? 0 : 1;

                ILPVariable variable = new ILPVariable(literal.getVar().getV());

                Term newTerm = new Term(coeff, variable);
                terms.add(newTerm);
            }

            int bound = -numOfNegativeTerms + 1;

            Constraint newConstraint = new Constraint(terms, Constraint.Op.GE, bound);
            outputILP.addConstraint(newConstraint);
        }

        return outputILP;
    }

    /**
     * Convert an ILP to an SAT instance.
     *
     * @param inputILP The 0-1 ILP serving as input.
     * @return The 3-SAT.
     */
    public static SATinstance ilpToSat(ZeroOneILP inputILP) {
        SATinstance satOutput = new SATinstance();

        // For each constraint.
        for (Constraint constraint : inputILP.getConstraints()) {
            Clause clause = new Clause();

            // For each term.
            for (Term term : constraint.getTerms()) {
                Literal literal = new Literal(term.getVariable().getV() * term.getCoefficient());
                clause.addLiteral(literal);
            }

            satOutput.addClause(clause);
        }

        return satOutput;
    }
}
