import java.util.ArrayList;
import java.util.List;

public class And extends BinaryOperation {
    And(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + lhs.print() + "&" + rhs.print() + ")";
    }

    public int calculate(ArrayList<List<Integer>> tree, int a, int b, int c, int i, ArrayList<String> allVars) {
        if (lhs.calculate(tree, a, b, c, i, allVars) == 1 && rhs.calculate(tree, a, b, c, i, allVars) == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
