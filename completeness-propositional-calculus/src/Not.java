import java.util.ArrayList;
import java.util.HashMap;

public class Not extends Expression {
    public Expression exp;
    private Boolean evaluation = null;

    Not(Expression e, String str) {
        exp = e;
        hash = str.hashCode();
        s = str;
    }

    public boolean isBase() {
        if (exp.getClass() == Const.class || (exp.getClass() == Not.class))
            return true;

        return false;
    }

    public Boolean proved(int mask, HashMap<String, Integer> allVars) {
        evaluation = !exp.proved(mask, allVars);
        return evaluation;
    }

    public String whatType() {
        return "!";
    }


    public boolean evaluate(int mask, HashMap<String, Integer> allVars) {
        return !(exp.evaluate(mask, allVars));
    }

    String print(){
        return "!(" + exp.print() + ")";
    }

    /*public ArrayList<Expression> makeMe(int mask, int total) {
        ArrayList<Expression> res = new ArrayList<Expression>();

//        if(isNotNot(this))         // сократим себе один шаг
//        {
//            Expression childChild = ((Not) ((Not) (this)).exp).exp;
//            res.addAll(childChild.makeMe(mask, total));
//            res.addAll(
//                    nnA_A(childChild.s)
//            );
//            return res;
//        }

        res.addAll(exp.makeMe(mask, total)); // константа или что угодно


        boolean eb = exp.evaluate(mask);   // вычислить СЫНА!!
        String es = exp.s;      // строка СЫНА!!!

        if (!eb)
            res.addAll(nA(es));  // иныертируем сына
        else {
            res.addAll(A_nnA(es));
            return res;
        }

        if (isNotNot(res.get(res.size() - 1))) {        // если последний в выводе - !!A, то избавимся.
            Expression temp = res.get(res.size() - 1);
            res.addAll(
                    nnA_A(
                            ((Not) ((Not) (temp)).exp).exp.s
                    )
            );
        }

        //TODO зачем на вывод A -> !!A  ???

        return res;
    }*/

    public ArrayList<Expression> getProof(int mask, int kth, int total) {
        ArrayList<Expression> res;

        //System.out.println("NOT.getProof called! there is still some magic in the world!");

        if (kth == -1) {           // зафиксировали все - просим построиться от маски
            //return makeMe(mask, total);
        }

        ArrayList<Expression> der1, der2;                               // два вывода от разных масок
        der1 = this.getProof(setbit(mask, kth), kth - 1, total);
        der2 = this.getProof(resbit(mask, kth), kth - 1, total);


        res = Terminator.terminate(der1, der2, mask, kth, total);        // поглоим переменную
        return res;
    }

    public ArrayList<Expression> nA(String s) {
        ArrayList<Expression> res = new ArrayList<Expression>();
        res.add(Parser.parse("!(" + s + ")"));
        return res;
    }

    public ArrayList<Expression> A_nnA(String s) {
        String[] array = new String[]{
                "@",
                "((!@)->(((!@)->(!@))->(!@)))",
                "((!@)->((!@)->(!@)))",
                "(((!@)->((!@)->(!@)))->(((!@)->(((!@)->(!@))->(!@)))->((!@)->(!@))))",
                "(((!@)->(((!@)->(!@))->(!@)))->((!@)->(!@)))",
                "((!@)->(!@))",
                "(@->((!@)->@))",
                "@",
                "((!@)->@)",
                "(((!@)->@)->(((!@)->(!@))->(!(!@))))",
                "(((!@)->(!@))->(!(!@)))",
                "(!(!@))"};
        return Replacer.replase(array, s);
    }

    public ArrayList<Expression> nnA_A(String s) {
        String[] array = new String[]{
                "!!@",
                "!!@->@",
                "@"};
        return Replacer.replase(array, s);
    }
}
