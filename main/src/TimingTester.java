import java.io.FileWriter;
import java.io.IOException;

public class TimingTester {
    public static void timingSatTo3SatLengthCheck() {
        FileWriter outputFile = createOutputFile("SAT_TO_3-SAT_split_lengths");
        for (int i = 1; i < 1000; i++) {
            SATinstance testInstance = SatGenerator.genSAT(i, 1, i);
            SATinstance threeSat = Transformer.satToThreeSat(testInstance);
            threeSat.getLength();
            String result = "Number of literals: " + i + "\tSplit Length: " + threeSat.getLength();
            flushResult(outputFile, result);
        }
        try {
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void timingSatTo3SatConversionClauseAndLiteral() {
        FileWriter outputFile = createOutputFile("SatTo3SAT conversion timing.");
        flushResult(outputFile, "Clause num\tLiteral num\ttimediff (ns)");
        // Test all combinations of literals and clause.
        // Clause: (1, 500)
        // Literal: (1, 3000)
        for (int clauseNum = 1; clauseNum < 500; clauseNum++) {
            for (int literalNum = 1; literalNum < 1000; literalNum++) {
                SATinstance randomSat = SatGenerator.genSAT(literalNum, clauseNum, literalNum);

                // Time conversion.
                long startTime = System.nanoTime();
                Transformer.satToThreeSat(randomSat);
                long endTime = System.nanoTime();

                long timeDiff = endTime - startTime;

                // Write result.
                flushResult(outputFile, clauseNum + "\t" + literalNum + "\t" + timeDiff);
            }
        }
    }

    public static void timingTestSolutionTime() {
        FileWriter outputFile = createOutputFile("Sat solution timing.");
        flushResult(outputFile, "Clause num\tLiteral num\ttimediff (ns)");

        // Same setup as the method above.
        for (int clauseNum = 1; clauseNum < 25; clauseNum++) {
            for (int literalNum = 1; literalNum < 25; literalNum++) {
                SATinstance randomSat = SatGenerator.genSAT(literalNum, clauseNum, literalNum);

                long startTime = System.nanoTime();
                randomSat.findSolution();
                long endTime = System.nanoTime();

                long timeDiff = endTime - startTime;
                System.out.println(clauseNum + "\t" + literalNum + "\t" + timeDiff);
                flushResult(outputFile, clauseNum + "\t" + literalNum + "\t" + timeDiff);
            }
        }
    }
    /**
     * Create a FileWriter with timestamp and correct extension.
     *
     * @param description The identifying comoponent of the file name.
     * @return The FileWriter.
     */
    private static FileWriter createOutputFile(String description) {
        long unixTime = System.currentTimeMillis() / 1000; // Create timestamp.
        try {
            FileWriter writer = new FileWriter(unixTime + description + ".txt");
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null; // Will never be reached, but compilation error otherwise.
    }

    private static void flushResult(FileWriter writer, String result) {
        try {
            writer.append(result + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println("Starting 3 sat length test.");
//        timingSatTo3SatLengthCheck();
//        System.out.println("Starting conversion timing.");
//        timingSatTo3SatConversionClauseAndLiteral();
        System.out.println("Starting solution finding.");
        timingTestSolutionTime();
    }

}
