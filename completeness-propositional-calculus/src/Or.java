import java.util.ArrayList;
import java.util.HashMap;

public class Or extends BinaryOperation {

    private Boolean evaluation = null;

    public Boolean proved(int mask, HashMap<String, Integer> allVars) {
        evaluation = lhs.proved(mask, allVars) | rhs.proved(mask, allVars);
        return evaluation;
    }

    public String whatType() {
        return "|";
    }

    Or(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + lhs.print() + "|" + rhs.print() + ")";
    }

    public boolean isBase() {
        if ((lhs.getClass() == Const.class && rhs.getClass() == Const.class))
            return true;
        return false;
    }

    public boolean evaluate(int mask, HashMap<String, Integer> allVars) {
        return lhs.evaluate(mask, allVars) || rhs.evaluate(mask, allVars);
    }

    public ArrayList<Expression> A_B(String a, String b) {
        String[] array = new String[]{
                "@",
                "#",
                "(@->(@|#))",
                "@",
                "(@|#)"};
        return Replacer.replase(array, a, b);
    }

    public ArrayList<Expression> A_nB(String a, String b) {
        String[] array = new String[]{
                "@",
                "(!#)",
                "(@->(@|#))",
                "@",
                "(@|#)"};
        return Replacer.replase(array, a, b);
    }

    public ArrayList<Expression> nA_B(String a, String b) {
        String[] array = new String[]{
                "(!@)",
                "#",
                "(#->(@|#))",
                "#",
                "(@|#)"};
        return Replacer.replase(array, a, b);
    }

    public ArrayList<Expression> nA_nB(String a, String b) {
        String[] array = new String[]{
                "(!@)",
                "(!#)",
                "(((!@)->((!#)->(!@)))->(@->((!@)->((!#)->(!@)))))",
                "((!@)->((!#)->(!@)))",
                "(@->((!@)->((!#)->(!@))))",
                "((!@)->(@->(!@)))",
                "(!@)",
                "(@->(!@))",
                "((@->(!@))->((@->((!@)->((!#)->(!@))))->(@->((!#)->(!@)))))",
                "((@->((!@)->((!#)->(!@))))->(@->((!#)->(!@))))",
                "(@->((!#)->(!@)))",
                "((@->((!#)->@))->(@->(@->((!#)->@))))",
                "(@->((!#)->@))",
                "(@->(@->((!#)->@)))",
                "(@->((@->@)->@))",
                "(@->(@->@))",
                "((@->(@->@))->((@->((@->@)->@))->(@->@)))",
                "((@->((@->@)->@))->(@->@))",
                "(@->@)",
                "((@->@)->((@->(@->((!#)->@)))->(@->((!#)->@))))",
                "((@->(@->((!#)->@)))->(@->((!#)->@)))",
                "(@->((!#)->@))",
                "((((!#)->@)->(((!#)->(!@))->(!(!#))))->(@->(((!#)->@)->(((!#)->(!@))->(!(!#))))))",
                "(((!#)->@)->(((!#)->(!@))->(!(!#))))",
                "(@->(((!#)->@)->(((!#)->(!@))->(!(!#)))))",
                "((@->((!#)->@))->((@->(((!#)->@)->(((!#)->(!@))->(!(!#)))))->(@->(((!#)->(!@))->(!(!#))))))",
                "((@->(((!#)->@)->(((!#)->(!@))->(!(!#)))))->(@->(((!#)->(!@))->(!(!#)))))",
                "(@->(((!#)->(!@))->(!(!#))))",
                "((@->((!#)->(!@)))->((@->(((!#)->(!@))->(!(!#))))->(@->(!(!#)))))",
                "((@->(((!#)->(!@))->(!(!#))))->(@->(!(!#))))",
                "(@->(!(!#)))",
                "(((!(!#))->#)->(@->((!(!#))->#)))",
                "((!(!#))->#)",
                "(@->((!(!#))->#))",
                "((@->(!(!#)))->((@->((!(!#))->#))->(@->#)))",
                "((@->((!(!#))->#))->(@->#))",
                "(@->#)",
                "(#->((#->#)->#))",
                "(#->(#->#))",
                "((#->(#->#))->((#->((#->#)->#))->(#->#)))",
                "((#->((#->#)->#))->(#->#))",
                "(#->#)",
                "((@->#)->((#->#)->((@|#)->#)))",
                "((#->#)->((@|#)->#))",
                "((@|#)->#)",
                "((!#)->((@|#)->(!#)))",
                "(!#)",
                "((@|#)->(!#))",
                "(((@|#)->#)->(((@|#)->(!#))->(!(@|#))))",
                "(((@|#)->(!#))->(!(@|#)))",
                "(!(@|#))"};
        return Replacer.replase(array, a, b);
    }
}
