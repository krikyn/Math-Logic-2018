import java.util.ArrayList;
import java.util.HashMap;

public class Const extends Expression {

    private Boolean evaluation = null;

    public Boolean proved(int mask, HashMap<String, Integer> allVars) {
        evaluation = getbit(mask, allVars.get(s));
        //evaluation = variable.get(s);
        return evaluation;
    }

    public String whatType() {
        return "val";
    }

    Const(String str) {
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + s + ")";
    }


    public boolean isBase() {
        System.out.println("WARNING! CONST.isBase called! it's impossible");
        return false;
    }

    public boolean evaluate(int mask, HashMap<String, Integer> allVars) {
        //System.out.println(s);
        //System.out.println(allVars.containsKey(s));
        int pos = allVars.get(s);
        return getbit(mask, pos);
    }

    public ArrayList<Expression> getProof(int mask, int kth, int total) {
        ArrayList<Expression> res = new ArrayList<Expression>();
        int pos = s.charAt(0) - 'A';

        if (getbit(mask, pos))
            res.add(Parser.parse(s));
        else
            res.add(Parser.parse("!(" + s + ")"));

        return res;
    }

    public ArrayList<Expression> makeMe(int mask, int total) {
        ArrayList<Expression> res = new ArrayList<Expression>();

        if (getbit(mask, (s.charAt(0) - 'A')))
            res.add(Parser.parse(s.charAt(0) + ""));
        else
            res.add(Parser.parse("!(" + s.charAt(0) + ")"));

        return res;
    }

    public ArrayList<String> getProof(int a, int b) {
        System.out.println("WARNING!! Const.getProof is called!");
        return null;
    }
}
