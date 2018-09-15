import java.util.ArrayList;
import java.util.List;

public class Or extends BinaryOperation {
    Or(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + lhs.print() + "|" + rhs.print() + ")";
    }

    public int calculate(ArrayList<List<Integer>> tree, ArrayList<Integer> forcedVars, Integer child, ArrayList<String> allVars) {
        //System.out.println(lhs.calculate(tree, forcedVars, child, allVars));
        //System.out.println(rhs.calculate(tree, forcedVars, child, allVars));
        if (lhs.calculate(tree, forcedVars, child, allVars) == 1 || rhs.calculate(tree, forcedVars, child, allVars) == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
