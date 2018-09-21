import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Impl extends BinaryOperation {
    public Impl(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + lhs.print() + "->" + rhs.print() + ")";
    }

    public int calculate(ArrayList<List<Integer>> tree, int a, int b, int c, int i, ArrayList<String> allVars) {
        Stack<Integer> st = new Stack<>();
        st.push(i);

        while(!st.isEmpty()){
            int cur = st.pop();

            if (lhs.calculate(tree, a, b, c, cur, allVars) == 1 && rhs.calculate(tree, a, b, c, cur, allVars) == 0){
                return 0;
            }

            for (int t: tree.get(cur)){
                st.push(t);
            }
        }

        return 1;
    }
}
