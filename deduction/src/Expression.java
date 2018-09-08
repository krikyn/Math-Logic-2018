import java.util.HashMap;

public abstract class Expression extends Object {
    public String s;
    public int hash;

    public boolean equals(Expression other) {
        if (this.hash == other.hash &&
                this.s.equals(other.s)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && hashCode() == obj.hashCode() && toString().equals(toString());
    }

    String print() {
        return s;
    }
}
