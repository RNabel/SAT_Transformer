import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SATSolution {
    List<Boolean> assignments = new ArrayList<>();
    boolean hasSolution = false;
    /**
     * Constructs a truth-value assignment for boolean values used in the original SAT.
     *
     * @param ilpSolution The 0-1 ILP solution to be used as basis.
     */
    public SATSolution(ILPSolution ilpSolution, int numOfLiterals) {
        if (ilpSolution != null) {
            assignments
                    .addAll(ilpSolution.getSoln().entrySet().stream()
                            .filter(entry -> entry.getKey().getV() <= numOfLiterals)
                            .map(entry -> entry.getValue() == 1)
                            .collect(Collectors.toList()));
            hasSolution = true;
        }
    }

    @Override
    public String toString() {
        String output = "";

        if (hasSolution) {
            for (int i = 0; i < assignments.size(); i++) {
                output += i + 1 + ": " + assignments.get(i) + "    ";
            }
        } else {
            output = "No solution could be found - the problem cannot be satisfied.";
        }

        return output;
    }
}
