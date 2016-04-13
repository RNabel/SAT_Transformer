import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SATSolution {
    List<Boolean> assignments = new ArrayList<>();

    /**
     * Constructs a truth-value assignment for boolean values used in the original SAT.
     *
     * @param ilpSolution The 0-1 ILP solution to be used as basis.
     */
    public SATSolution(ILPSolution ilpSolution, int numOfLiterals) {
        assignments
                .addAll(ilpSolution.getSoln().entrySet().stream()
                        .filter(entry -> entry.getKey().getV() <= numOfLiterals)
                        .map(entry -> entry.getValue() == 1)
                        .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        String output = "";

        for (int i = 0; i < assignments.size(); i++) {
            output += i + 1 + ": " + assignments.get(i) + "    ";
        }

        return output;
    }
}
