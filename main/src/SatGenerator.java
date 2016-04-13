import java.util.Random;

public class SatGenerator {
    public static SATinstance genSAT(int numberOfLiterals, int numberOfClauses, int maxClauseSize) {
        SATinstance output = new SATinstance();

        Random random = new Random();

        for (int i = 0; i < numberOfClauses; i++) {
            int numOfLiteralsInClause = random.nextInt(maxClauseSize) + 1;
            Clause newClause = genClause(numOfLiteralsInClause, numberOfLiterals);
            output.addClause(newClause);
        }

        return output;
    }

    private static Clause genClause(int numberOfLiterals, int numOfDifferentLiterals) {
        Clause clause = new Clause();

        Random random = new Random();

        for (int i = 0; i < numberOfLiterals; i++) {
            int literalValue = random.nextInt(numOfDifferentLiterals) + 1;
            boolean literalSign = random.nextBoolean();
            literalValue = (literalSign) ? literalValue : -literalValue;

            Literal literal = new Literal(literalValue);
            clause.addLiteral(literal);
        }

        return clause;
    }
}
