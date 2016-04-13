public class ILPVariable {
    private int v;

    public ILPVariable (int v) {
        if (v < 0) {
            throw new IllegalArgumentException 
                ("Variable " + v + " must be positive");
        }
        this.v = v;
    }

    public String toString () {
        return "x" + Integer.toString(this.v);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + v;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ILPVariable other = (ILPVariable) obj;
        if (v != other.v)
            return false;
        return true;
    }

}
