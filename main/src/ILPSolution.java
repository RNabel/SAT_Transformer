import java.util.HashMap;
import java.util.Map;

/**
 * An ILPSolution provides a mapping between ILP variables and Integers
 */
public class ILPSolution {
    private HashMap<ILPVariable, Integer> soln;

    public ILPSolution () {
        soln = new HashMap<>();
    }

    /**
     * add/replace mapping for a ILP variable 
     */
    public void add (ILPVariable zv, int i) {
        soln.put (zv, i);
    }

    /**
     * remove mapping for a ILP variable 
     */
    public void clear (ILPVariable zv) {
        soln.remove (zv);
    }

    /**
     * retrieve mapping for a ILP variable 
     */
    public int getValue (ILPVariable zv) {
        return soln.get(zv);
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ILPVariable, Integer> vi : soln.entrySet()) {
            sb.append(vi.getKey());
            sb.append(":=");
            sb.append(vi.getValue());
            sb.append("; ");
        }
        return sb.toString();
    }
}
