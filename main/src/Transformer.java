/**
 * Class transforming NPC problems between each other.
 */
public class Transformer {
    public static SATinstance satToThreeSat(SATinstance inputSAT) {
        SATinstance satInstance = new SATinstance();

        // Loop through each clause and break up as necessary.
        for (Clause clause : inputSAT.clauses) {
            System.out.println(clause);
        }

        return satInstance;
    }
}
