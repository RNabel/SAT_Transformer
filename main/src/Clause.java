import java.io.PushbackReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

public class Clause {
    private LinkedList<Literal> lits;

    public Clause () {
        lits = new LinkedList<Literal>();
    }

    public int getLength () {
        return lits.size();
    }

    public void addLiteral (Literal l) {
        lits.add (l);
    }

    public ListIterator<Literal> getLiterals () {
        return lits.listIterator();
    }

    public String toString () {
        StringBuilder sb = new StringBuilder("[");
        boolean hasprev = false;
        for (Literal l : lits) {
            if (hasprev) {
                sb.append(", ");
            }
            hasprev = true;
            sb.append (l);
        }
        return sb.append("]").toString();
    }

    private enum ParseState {INIT, BETWEEN}

    /**
     * Simple reader for literals as integers, assuming ASCII input
     */
    private static int parseInt (PushbackReader pr) {
        int ch = 0; 
        int num = 0;
        boolean pos = true;
        try {
            while ((ch = pr.read()) != -1) {
                switch (ch) {
                case '+' : pos = true; break;
                case '-' : pos = false; break;
                case '0' : case '1' : case '2' : case '3' : case '4' :
                case '5' : case '6' : case '7' : case '8' : case '9' :
                    num = num * 10 + (ch - '0'); break;
                default : 
                    pr.unread (ch);
                    return pos ? num : 0 - num;
                }
            }
        } catch (IOException e) {
            System.err.println ("Expected literal integer");
        }
        return pos ? num : 0 - num;
    }

    /**
     * Parse a clause, which must look like
     * <pre>[ <literal1>, <literal2>, ... ] </pre>
     *
     * @return parsed clause, or null on syntax error 
     */
    public static Clause parseClause (PushbackReader pr) {
        try {
            Clause cl = new Clause ();
            int ch;
            ParseState ps = ParseState.INIT;
            while ((ch = pr.read()) != -1) {
                if (Character.isWhitespace (ch)) 
                    continue;
                switch (ps) {
                case INIT:
                    if (ch != '[') {
                        System.err.println ("Syntax error, expecting [");
                        return null;
                    }
                    ps = ParseState.BETWEEN;
                    break;
                case BETWEEN:
                    if (ch == ',') 
                        continue;
                    if (ch == ']')
                        return cl;
                    pr.unread (ch);
                    int l = parseInt (pr);
                    cl.addLiteral (new Literal (l));
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println ("Could not read input clause");
            return null;
        }
        System.err.println ("Malformed input clause");
        return null;
    }
}
