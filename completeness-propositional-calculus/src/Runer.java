import java.util.ArrayList;
import java.io.*;

public class Runer {
    public static void main(String[] args) {
        ArrayList<Expression> l = lemma45("A");

        String filename = "1.txt";
        try {
            PrintWriter writer = new PrintWriter(filename);
            for (Expression e : l) {
                writer.println(e.s);
            }
            writer.close();
        } catch (FileNotFoundException exc) {
            System.out.println("No file: " + filename + ", or can't write to it!");
            System.exit(1);
        }
    }

    public static ArrayList<Expression> lemma45(String p) {
        ArrayList<Expression> res = new ArrayList<Expression>();

        // res.add("#lemma_4.5 {");
        res.add(Parser.parse("(" + p + "->" + "(" + p + "|!(" + p + ")))"));
        res.addAll(lemma44(p, "((" + p + ")|(!(" + p + ")))"));
        //res.add(Parser.parse("lemma44goes"));
        res.add(Parser.parse("(" + "!(" + p + "|!(" + p + "))" + "->!(" + p + "))"));

        res.add(Parser.parse("!(" + p + ")" + "->" + "(" + p + "|!(" + p + "))"));
        res.addAll(lemma44("!(" + p + ")", "(" + p + "|!(" + p + "))"));
        //res.add(Parser.parse("lemma44goes"));
        res.add(Parser.parse("(" + "!(" + p + "|!(" + p + "))" + "->" + "!(!(" + p + ")))"));


        Expression el = Parser.parse("(" + "!((" + p + ")|(!(" + p + ")))" + "->(!(" + p + ")))");
        Expression erl = Parser.parse("(!((" + p + ")|(!(" + p + "))))" + "->" + "(!(!(" + p + ")))");
        Expression err = Parser.parse("(!(!((" + p + ")|!(" + p + "))))");
        Expression er = new Impl(erl, err, "(" + erl.s + "->" + err.s + ")");
        Expression e = new Impl(el, er, "(" + el.s + "->" + er.s + ")");
        res.add(e);

        res.add(er);

        res.add(err);
        res.add(Parser.parse(err.s + "->" + "((" + p + ")|(!(" + p + ")))"));
        res.add(Parser.parse("((" + p + ")|(!(" + p + ")))"));

        //res.add("#lemma_4.5 }");

        return res;
    }

    public static ArrayList<Expression> lemma44(String a, String b) {
        ArrayList<Expression> res = new ArrayList<Expression>();

        //res.add("#lemma_4.4 {");

        res.add(Parser.parse("(" + a + "->" + b + ")->(" + a + "->!(" + b + "))" + "->!(" + a + ")"));
        res.add(Parser.parse(a + "->" + b));
        res.add(Parser.parse("(" + a + "->!(" + b + "))" + "->!(" + a + ")"));
        res.add(Parser.parse("!(" + b + ")->" + "(" + a + "->(!(" + b + ")))"));
        res.add(Parser.parse("!(" + b + ")"));
        res.add(Parser.parse("(" + a + ")->(!(" + b + "))"));
        res.add(Parser.parse("!(" + a + ")"));

        ArrayList<Expression> one = new ArrayList<Expression>();
        one.add(Parser.parse("(" + a + ")->(" + b + ")"));

        ArrayList<Expression> empty = new ArrayList<Expression>();

        if (a == null || b == null) {
            System.out.println("MEGA FAIL!!! a, b, null.  lemma 4.4!!");
        }

        ArrayList<Expression> deductedRes = new ArrayList<Expression>();
        ArrayList<Expression> newRes = new ArrayList<Expression>();

        deductedRes.addAll(
                deduction(
                        res,
                        Parser.parse("!(" + b + ")"),
                        one
                )
        );

        newRes.addAll(
                deduction(
                        deductedRes,
                        Parser.parse("(" + a + ")->(" + b + ")"),
                        empty
                )
        );

        //res.add("#lemma_4.4 }");

        return newRes;
    }

    public static ArrayList<Expression> deduction(ArrayList<Expression> given, Expression alphaExpr, ArrayList<Expression> gamma) {
        ArrayList<String> built = new ArrayList<String>();
        ArrayList<Expression> givenPlus = new ArrayList<Expression>();
        givenPlus.addAll(given);


        //  Expression alpha = Parser.parse(al);
        String a = alphaExpr.s;

        if (a == null || alphaExpr == null || a.equals("")) {
            System.out.println("something goes wrong with parsing!!");
        }


        for (Expression curLineExpr : given) {

            if (curLineExpr == null || "".equals(curLineExpr.s)) {
                System.out.println("impossible!!!!!!");
                continue;
            }

//            if (curLine.charAt(0) == '#') {
//                givenPlus.add("#" + curLine);
//                continue;
//            }

            //Expression expression = Parser.parse(curLine);
//            if (curLineExpr == null || curLineExpr.s == null || curLineExpr.s.equals("")) {
//                System.out.println("something goes wrong with parsing!!");
//                System.out.println(curLineExpr.s);
//            }

            String di = curLineExpr.s;

            switch (checkExpr(curLineExpr, alphaExpr, gamma)) {
                case 1:  // axiom or given
                    // built.add("#case1: " + di);
                    built.add(di);
                    built.add(di + "->(" + a + "->" + di + ")");
                    built.add(a + "->" + di);
                    break;
                case 2: // di equals to a
                    //built.add("#case2: " + di);
                    built.add(a + "->(" + a + "->" + a + ")");
                    built.add("(" + a + "->(" + a + "->" + a + ")" + ")" + "->" + "(" + a + "->((" + a + "->" + a + ")->" + a + "))" + "->" + "(" + a + "->" + a + ")");
                    built.add("(" + a + "->((" + a + "->" + a + ")->" + a + "))" + "->" + "(" + a + "->" + a + ")");
                    built.add("(" + a + "->((" + a + "->" + a + ")->" + a + "))");
                    built.add(a + "->" + di);
                    break;
                case 3:
                    //built.add("#case3: " + di);
                    String dj = findLeft(curLineExpr, givenPlus);
                    built.add("(" + a + "->" + dj + ")" + "->" + "((" + a + "->(" + dj + "->" + di + "))" + "->" + "(" + a + "->" + di + "))");
                    built.add("((" + a + "->" + "(" + dj + "->" + di + "))" + "->" + "(" + a + "->" + di + "))");
                    built.add(a + "->" + di);
                    break;
            }
            givenPlus.add(curLineExpr);
        }

        ArrayList<Expression> res = new ArrayList<Expression>();
        for (String s : built) {
            res.add(Parser.parse(s));
        }
        return res;
    }


    static String findLeft(Expression di, ArrayList<Expression> givenPlus) {
        for (Expression dk : givenPlus) {
//            if (sdk.charAt(0) == '#') {
//                continue;
//            }
            //dk = Parser.parse(sdk);
            if (dk.getClass() == Impl.class && di.equals(((Impl) dk).rhs)) {

                for (Expression dj : givenPlus) {
                    //dj = Parser.parse(sdj);

//                    if (!checkBalance(dj)) {
//                        log("Terminator..findLeft:" + di.s, given);
//                    }

                    if ((((Impl) dk).lhs).equals(dj)) {
                        //built.add("#returned: dk=" + ((BinaryOperation) dk).s + " -+- " + "dj" + "=" + dj.s + "  -->  " + ((BinaryOperation) dk).rhs.s);
                        return ((BinaryOperation) dk).lhs.s;
                    }
                }
            }
        }
        System.out.println("incorrect derivation to be deduced: " + di.s);
        //log2("Fuck!", givenPlus);

        return null; // ���� �������� ����� ���������, ������� �� �������� ������.
    }

    static int checkExpr(Expression e, Expression alpha, ArrayList<Expression> gamma) {
        if (Axiom1(e)) return 1;
        if (Axiom2(e)) return 1;
        if (Axiom3(e)) return 1;
        if (Axiom4(e)) return 1;
        if (Axiom5(e)) return 1;
        if (Axiom6(e)) return 1;
        if (Axiom7(e)) return 1;
        if (Axiom8(e)) return 1;
        if (Axiom9(e)) return 1;
        if (Axiom10(e)) return 1;

        for (Expression d : gamma) {
            if (d.equals(e)) {
                return 1;
            }
        }

        if (e.equals(alpha)) {
            return 2;
        }

        return 3;
    }

    private static boolean Axiom1(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class)      // X->Y->Z
        {
            Expression x = ((BinaryOperation) e).lhs;

            if (((BinaryOperation) (((BinaryOperation) e).rhs)).rhs.equals(x)) {
                return true;
            }
        }

        return false;
    }

    private static boolean Axiom2(Expression e) {
        if (e.getClass() != Impl.class) {
            return false;
        }

        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&   // ����� ����
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&   // ������ ����
                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.getClass() == Impl.class
                ) {
            Expression a, b, c;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            c = ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).rhs;

            // r l l = A
            // r l r l= B
            // r r l = A
            // r r r = C
            if (((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).lhs.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom3(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == And.class) {
            Expression a = ((BinaryOperation) e).lhs;
            Expression b = ((BinaryOperation) ((BinaryOperation) e).rhs).lhs;
            if (((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom4(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == And.class) {
            Expression a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            if (((BinaryOperation) e).rhs.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom5(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == And.class) {
            Expression b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            if (((BinaryOperation) e).rhs.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom6(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Or.class) {
            Expression a = ((BinaryOperation) ((BinaryOperation) e).rhs).lhs;
            if (((BinaryOperation) e).lhs.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom7(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Or.class) {
            Expression b = ((BinaryOperation) ((BinaryOperation) e).rhs).rhs;
            if (((BinaryOperation) e).lhs.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom8(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&   // ����� ����
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&   // ������ ����

                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Impl.class &&

                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.getClass() == Or.class) {
            Expression a, b, c;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            c = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            b = ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs;

            // r l r = C
            // r r r = C
            // r r l l = A
            // r r l r = B
            if (((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs).rhs.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom9(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&   // ����� ����
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&   // ������ ����

                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Not.class &&
                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.getClass() == Not.class)

//        if (e.getClass() == Impl.class)
//        if(((BinaryOperation) e).lhs.getClass() == Impl.class)
//        if(((BinaryOperation) e).rhs.getClass() == Impl.class)
//        if(((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class)
//        if(((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Not.class)
//        if(((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.getClass() == Not.class)
        {
            Expression a, b;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;

            if ((((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs).equals(a) &&
                    ((Not) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).exp.equals(a) &&
                    ((Not) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).exp.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private static boolean Axiom10(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Not.class &&
                ((Not) ((BinaryOperation) e).lhs).exp.getClass() == Not.class) {

            if (((Not) ((BinaryOperation) e).lhs).exp.getClass() == Not.class) {
                if (((Not) (((Not) ((BinaryOperation) e).lhs).exp)).exp.equals(((BinaryOperation) e).rhs)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Deprecated
    private static boolean checkBalance(String s) {
        int balance = 0;

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '(')
                ++balance;
            if (s.charAt(i) == ')')
                --balance;

            if (balance < 0)
                return false;
        }

        if (balance == 0)
            return true;
        else
            return false;
    }

    public static boolean flag = false;
    public static boolean flag2 = false;

    @Deprecated
    private static void log(String s1, ArrayList<String> s2) {
        if (flag) return;
        String filename = "log.txt";
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println(s1);
            for (String s : s2) {
                writer.println(s);
            }
            writer.println("###############################");
            flag = true;
            writer.close();
        } catch (FileNotFoundException exc) {
            System.out.println("No file: " + filename + ", or can't write to it!");
            System.exit(1);
        }
    }

    @Deprecated
    private static void log2(String s1, ArrayList<String> s2) {
        if (flag2) return;
        String filename = "log2.txt";
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.println(s1);
            for (String s : s2) {
                writer.println(s);
            }
            writer.println("###############################");
            flag2 = true;
            writer.close();
        } catch (FileNotFoundException exc) {
            System.out.println("No file: " + filename + ", or can't write to it!");
            System.exit(1);
        }
    }
}
