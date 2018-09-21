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

    public int calculate(ArrayList<List<Integer>> tree, int a, int b, int c, int i, ArrayList<String> allVars) {
        Stack<Integer> st = new Stack<>();
        st.push(i);

        while(!st.isEmpty()){
            int cur = st.pop();

            if (exp.calculate(tree, a, b, c, cur, allVars) == 1){
                return 0;
            }

            for (int t: tree.get(cur)){
                st.push(t);
            }
        }

        return 1;
    }

}
