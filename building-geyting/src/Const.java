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

    public int calculate(ArrayList<List<Integer>> tree, ArrayList<Integer> forcedVars, Integer child, ArrayList<String> allVars) {

        int pos = allVars.indexOf(s);
        return Utils.getbit(forcedVars.get(child), pos);

        /*if (s.equals(allVars.get(0))) {
            return Utils.getbit(forcedVars.get(child), 0);
        } else if (s.equals(allVars.get(1))) {
            return Utils.getbit(forcedVars.get(child), 1);
        } else if (s.equals(allVars.get(2))) {
            return Utils.getbit(forcedVars.get(child), 2);
        } else {
            return Utils.getbit(forcedVars.get(child), 3);
        }*/

    }
}
