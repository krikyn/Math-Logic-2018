import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Not extends Expression {
    public Expression exp;

    Not(Expression e, String str) {
        exp = e;
        hash = str.hashCode();
        s = str;
    }

    String print(){
        return "!(" + exp.print() + ")";
    }

    public int calculate(ArrayList<List<Integer>> tree, ArrayList<Integer> forcedVars, Integer child, ArrayList<String> allVars) {
        Stack<Integer> st = new Stack<>();
        st.push(child);

        while(!st.isEmpty()){
            int cur = st.pop();

            if (exp.calculate(tree, forcedVars, cur, allVars) == 1){
                return 0;
            }

            for (int t: tree.get(cur)){
                st.push(t);
            }
        }

        return 1;
    }

}
