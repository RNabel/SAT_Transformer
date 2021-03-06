import javax.swing.tree.TreeNode;
import java.io.*;
import java.lang.Character;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;

public class SATinstance {
    LinkedList<Clause> clauses;

    public SATinstance() {
        clauses = new LinkedList<Clause>();
    }

    public int getLength() {
        return clauses.size();
    }

    public void addClause(Clause c) {
        clauses.add(c);
    }

    public ListIterator<Clause> getClauses() {
        return clauses.listIterator();
    }

    public int getNumberOfLiterals() {
        int maxIndex = 0;
        for (Clause clause : this.clauses)
            for (Iterator<Literal> it = clause.getLiterals(); it.hasNext(); ) {
                Literal currentLiteral = it.next();
                int currentIndex = currentLiteral.getVar().getV();
                maxIndex = (maxIndex > currentIndex) ? maxIndex : currentIndex;
            }
        return maxIndex;
    }

    public SATSolution findSolution() {
        // Convert to 3-SAT.
        SATinstance threeSAT = Transformer.satToThreeSat(this);

        // Convert to ILP.
        ZeroOneILP zeroOneILP = Transformer.satToILP(threeSAT);

        // Solve ILP.
        ILPSolution solution = zeroOneILP.findSoln();

        // Convert the solution to SATSolution.
        int numOfLiterals = this.getNumberOfLiterals();
        SATSolution satSolution = new SATSolution(solution, numOfLiterals);

        return satSolution;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean hasprev = false;
        for (Clause c : clauses) {
            if (hasprev) {
                sb.append(", ");
            }
            hasprev = true;
            sb.append(c);
        }
        return sb.append("}").toString();
    }

    private enum ParseState {INIT, BETWEEN}

    /**
     * Parse SAT instances, which must look like
     * <pre>{ <clause1>, <clause2>, ... } </pre>
     *
     * @return parsed instance, or null on syntax error
     */
    public static SATinstance parseSATinstance(PushbackReader pr) {
        try {
            SATinstance si = new SATinstance();
            int ch;
            ParseState ps = ParseState.INIT;
            while ((ch = pr.read()) != -1) {
                if (Character.isWhitespace(ch))
                    continue;
                switch (ps) {
                    case INIT:
                        if (ch != '{') {
                            System.err.println("Syntax error, expecting {");
                            return null;
                        }
                        ps = ParseState.BETWEEN;
                        break;
                    case BETWEEN:
                        if (ch == ',')
                            continue;
                        if (ch == '}')
                            return si;
                        pr.unread(ch);
                        Clause c = Clause.parseClause(pr);
                        if (c != null) {
                            si.addClause(c);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read input");
            return null;
        }
        System.err.println("Malformed input SAT instance");
        return null;
    }

    /**
     * Utility function to read from standard input
     */
    public static SATinstance readSATinstance() {
        PushbackReader pr =
                new PushbackReader(new InputStreamReader(System.in));
        return parseSATinstance(pr);
    }

    public static void main(String[] args) {
        InputStream stdin = System.in;

        // If test argument was provided, set System.in to the string and parse SAT instance accordingly.
        if (args.length > 0) {
            byte[] bytes = (args[0] + "\n").getBytes();
            System.setIn(new ByteArrayInputStream(bytes));
        }

        SATinstance si = readSATinstance();
        System.out.println("Read: " + si);

        // Reset System.in
        System.setIn(stdin);

        // Convert to 3-SAT.
        System.out.println("Finished SAT parsing, now converting.");
        SATinstance si2 = Transformer.satToThreeSat(si);
        System.out.println("Converted: " + si2);

        // Convert to ILP.
        System.out.println("Convert to ILP.");
        ZeroOneILP zoILP = Transformer.satToILP(si2);
        System.out.println("0-1 ILP: " + zoILP);

        // Solve the 0-1 ILP.
        System.out.println("Solving the ILP");
        ILPSolution solution = zoILP.findSoln();
        System.out.println("The solution is: " + solution);

        // Solve original SAT.
        System.out.println("Solving in one go");
        SATSolution satSolution = si.findSolution();
        System.out.println(satSolution);

        // Convert back to 3-SAT.
        System.out.println("Convert back to 3-SAT");
        SATinstance backTo3SAT = Transformer.ilpToSat(zoILP);
        System.out.println("3-SAT: " + backTo3SAT);
    }
}
