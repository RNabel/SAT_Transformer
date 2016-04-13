import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;


public class Tests {
    InputStream stdin = System.in;
    String SIMPLE_SAT_MULTI_SOLUTION = "{[1,2,3,4,5],[6,7,8,9]}";
    String SIMPLE_SAT_NO_SOLUTION = "{[1,2],[-2],[-1]}";
    String COMPLEX_SAT_ONE_SOLUTION = "{[1,2,3,4,5,6],[-1],[-2],[-3],[-4],[-5]}";
    String COMPLEX_SAT_MULTI_SOLUTION = "{[1,2,3,4,5,6],[-1],[-2],[-3],[-5]}";

    @Test
    public void testSimpleMultipleSolution() {
        System.out.println("--- Test Simple SAT Multiple Solutions ---");
        setupStdIn(SIMPLE_SAT_MULTI_SOLUTION);
        SATinstance satInstance = SATinstance.readSATinstance();

        String resultString = createResultSet(satInstance);

        System.out.println(resultString);
    }

    @Test
    public void testSimpleNoSolution() {
        System.out.println("--- Test Simple No Solution ---");
        setupStdIn(SIMPLE_SAT_NO_SOLUTION);
        SATinstance saTinstance = SATinstance.readSATinstance();

        System.out.println(createResultSet(saTinstance));
    }

    @Test
    public void testComplexWithOneSolution() {
        System.out.println("--- Test Complex Problem One Solution ---");
        setupStdIn(COMPLEX_SAT_ONE_SOLUTION);
        SATinstance saTinstance = SATinstance.readSATinstance();

        System.out.println(createResultSet(saTinstance));
    }

    @Test
    public void testComplexWithMultiSolution() {
        System.out.println("--- Test Complex Problem Multiple Solution ---");
        setupStdIn(COMPLEX_SAT_MULTI_SOLUTION);
        SATinstance saTinstance = SATinstance.readSATinstance();

        System.out.println(createResultSet(saTinstance));
    }

    @After
    public void tearDown() {
        System.out.println("----------------------------------------------------------------------------------------------------");
        resetStdIn();
    }

    @Before
    public void setup() {
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    private String createResultSet(SATinstance satInstance) {
        String output = "Read: " + satInstance + "\n\n";

        // 3-SAT.
        SATinstance threeSat = Transformer.satToThreeSat(satInstance);
        output += "3-SAT: " + threeSat + "\n\n";

        // ILP.
        ZeroOneILP zeroOneILP = Transformer.satToILP(threeSat);
        output += "ILP: " + zeroOneILP + "\n";

        // Solution to 3-SAT.
        SATSolution threeSatSolution = threeSat.findSolution();
        output += "3-SAT Solution: " + threeSatSolution + "\n\n";

        // Solution to SAT.
        SATSolution normalSolution = satInstance.findSolution();
        output += "SAT Solution: " + normalSolution + "\n\n";

        // 0-1 reversed to SAT.
        SATinstance ilpReversed = Transformer.ilpToSat(zeroOneILP);
        output += "0-1 ILP reversed to SAT: " + ilpReversed + "\n";

        return output;
    }

    public void setupStdIn(String input) {
        byte[] bytes = (input + "\n").getBytes();
        System.setIn(new ByteArrayInputStream(bytes));
    }

    public void resetStdIn() {
        System.setIn(stdin);
    }
}
