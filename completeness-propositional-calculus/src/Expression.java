import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Expression {
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
    public boolean equals(Object obj) {
        return obj != null && hashCode() == obj.hashCode() && toString().equals(toString());
    }

    String print(){
        return s;
    }

    static int setbit(int x, int i) {
        return x | (1 << i);
    }

    static int resbit(int x, int i) {
        return setbit(x, i) ^ (1 << i);
    }

    static boolean getbit(int x, int i) {
        return (x & (1 << i)) != 0;
    }

    public boolean isNotNot(Expression e) {
        if (e.getClass() == Not.class && ((Not) e).exp.getClass() == Not.class)
            return true;
        else return false;
    }

    public abstract boolean evaluate(int mask, HashMap<String, Integer> allVars);

    public abstract boolean isBase();

    public abstract ArrayList<Expression> getProof(int mask, int kth, int total);

    /*public abstract ArrayList<Expression> makeMe(int mask, int total);*/

    public static List<Expression> addGamma(int mask, int total) {
        List<Expression> res = new ArrayList<Expression>();
        for (int i = 0; i < total; ++i) {
            Const cnst = new Const(new String((char) ('A' + i) + ""));
            if (getbit(mask, i))
                res.add(cnst);
            else
                res.add(new Not(cnst, "!(" + cnst.s + ")"));
        }
        return res;
    }

    public String whatType() {
        return "dflt";
    }

    public Boolean proved(int mask, HashMap<String, Integer> allVars) {
        return true;
    }

}
