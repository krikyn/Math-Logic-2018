import java.util.ArrayList;
import java.util.List;

public class Const extends Expression {
    Const(String str) {
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + s + ")";
    }

    public int calculate(ArrayList<List<Integer>> tree, int a, int b, int c, int i, ArrayList<String> allVars) {

        if (s.equals(allVars.get(0))) {
            return Utils.getbit(a, i);
        } else if (s.equals(allVars.get(1))) {
            return Utils.getbit(b, i);
        } else {
            return Utils.getbit(c, i);
        }

    }
}
