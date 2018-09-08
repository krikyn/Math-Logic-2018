import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static HashMap<Expression, ArrayList> mpParts = new HashMap<>(53000, 1);
    private static HashMap<Expression, Integer> assumptions = new HashMap<>(53000, 1);
    private static ArrayList<Expression> storeExpr = new ArrayList<>(53000);

    public static void main(String[] args) {
        HashMap<Expression, Integer> lines = new HashMap<>(53000, 1);

        try {
            BufferedReader in = Files.newBufferedReader(Paths.get("input.txt"), Charset.forName("UTF-8"));
            BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"));

            String[] firstLine = in.readLine().replace(" ", "").split("\\|-");

            String[] exprs = firstLine[0].split(",");
            for (int i = 0; i < exprs.length - 1; i++) {
                Expression newExpr = Parser.parse(exprs[i]);
                assumptions.put(newExpr, i);
                out.write(newExpr.s + (i < exprs.length - 2 ? "," : ""));
            }
            Expression a = Parser.parse(exprs[exprs.length - 1]);

            Expression b = Parser.parse(firstLine[1]);
            Expression aImplb = new Impl(a, b, "(" + a.s + "->" + b.s + ")");

            out.write("|-");
            out.write(aImplb.print());
            out.newLine();

            int curNum = 0;
            String line;
            while ((line = in.readLine()) != null) {
                curNum++;

                line = line.replace(" ", "");
                if (line.isEmpty()) {
                    continue;
                }
                Expression expr = Parser.parse(line);

                if (expr.getClass() == Impl.class) {
                    ArrayList<Expression> element = new ArrayList<>();
                    element.add(expr);
                    element.add(((Impl) expr).lhs);
                    mpParts.put(((Impl) expr).rhs, element);
                }

                int caseNum = 0;

                Integer assumpt = assumptions.get(expr);
                if (assumpt != null) {
                    caseNum = 1;
                }

                if (caseNum == 0) {
                    if (Axiom1(expr)) {
                        //out.write("Сх. акс. 1");
                        caseNum = 1;
                    } else if (Axiom2(expr)) {
                        //out.write("Сх. акс. 2");
                        caseNum = 1;
                    } else if (Axiom3(expr)) {
                        //out.write("Сх. акс. 3");
                        caseNum = 1;
                    } else if (Axiom4(expr)) {
                        //out.write("Сх. акс. 4");
                        caseNum = 1;
                    } else if (Axiom5(expr)) {
                        //out.write("Сх. акс. 5");
                        caseNum = 1;
                    } else if (Axiom6(expr)) {
                        //out.write("Сх. акс. 6");
                        caseNum = 1;
                    } else if (Axiom7(expr)) {
                        //out.write("Сх. акс. 7");
                        caseNum = 1;
                    } else if (Axiom8(expr)) {
                        //out.write("Сх. акс. 8");
                        caseNum = 1;
                    } else if (Axiom9(expr)) {
                        //out.write("Сх. акс. 9");
                        caseNum = 1;
                    } else if (Axiom10(expr)) {
                        //out.write("Сх. акс. 10");
                        caseNum = 1;
                    }
                }

                if (caseNum == 0 && a.equals(expr)) {
                    caseNum = 2;
                }

                Integer element2 = 0;
                if (caseNum == 0) {
                    ArrayList<Expression> element = mpParts.get(expr);
                    if (element != null) {
                        element2 = lines.get(element.get(1));
                        if (element2 != null) {
                            //out.write("M.P. " + lines.get(element.get(0)) + ", " + element2);
                            caseNum = 3;
                        }
                    }
                }

                String exprP = expr.print(), aP = a.print();
                switch (caseNum) {
                    case 1:
                        //out.write("1");
                        //out.newLine();
                        out.write(exprP);
                        out.newLine();
                        out.write("(" + exprP + "->(" + aP + "->" + exprP + "))");
                        out.newLine();
                        out.write("(" + aP + "->" + exprP + ")");
                        out.newLine();
                        break;
                    case 2:
                        //out.write("2");
                        //out.newLine();
                        out.write("(" + aP + "->(" + aP + "->" + aP + "))");
                        out.newLine();
                        out.write("((" + aP + "->(" + aP + "->" + aP + ")" + ")" + "->" + "((" + aP + "->((" + aP + "->" + aP + ")->" + aP + "))" + "->" + "(" + aP + "->" + aP + ")))");
                        out.newLine();
                        out.write("((" + aP + "->((" + aP + "->" + aP + ")->" + aP + "))" + "->" + "(" + aP + "->" + aP + "))");
                        out.newLine();
                        out.write("(" + aP + "->((" + aP + "->" + aP + ")->" + aP + "))");
                        out.newLine();
                        out.write("(" + aP + "->" + exprP +")");
                        out.newLine();
                        break;
                    case 3:
                        //out.write("3");
                        //out.newLine();
                        String additionally = storeExpr.get(element2 - 1).print();

                        out.write("((" + aP + "->" + additionally + ")" + "->" + "((" + aP + "->(" + additionally + "->" + exprP + "))" + "->" + "(" + aP + "->" + exprP + ")))");
                        out.newLine();
                        out.write("((" + aP + "->" + "(" + additionally + "->" + exprP + "))" + "->" + "(" + aP + "->" + exprP + "))");
                        out.newLine();
                        out.write("(" + aP + "->" + exprP + ")");
                        out.newLine();
                        break;
                }

                lines.put(expr, curNum);
                storeExpr.add(expr);
            }

            in.close();
            out.close();
        } catch (IOException ignored) {
        }
    }


    private static boolean Axiom1(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class) {
            Expression x = ((BinaryOperation) e).lhs;

            return ((BinaryOperation) (((BinaryOperation) e).rhs)).rhs.equals(x);
        }

        return false;
    }

    private static boolean Axiom2(Expression e) {
        if (e.getClass() != Impl.class) {
            return false;
        }

        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.getClass() == Impl.class
                ) {
            Expression a, b, c;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            c = ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).rhs;

            return ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).lhs.equals(b);
        }
        return false;
    }

    private static boolean Axiom3(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == And.class) {
            Expression a = ((BinaryOperation) e).lhs;
            Expression b = ((BinaryOperation) ((BinaryOperation) e).rhs).lhs;
            return ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(b);
        }
        return false;
    }

    private static boolean Axiom4(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == And.class) {
            Expression a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            return ((BinaryOperation) e).rhs.equals(a);
        }
        return false;
    }

    private static boolean Axiom5(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == And.class) {
            Expression b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            return ((BinaryOperation) e).rhs.equals(b);
        }
        return false;
    }

    private static boolean Axiom6(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Or.class) {
            Expression a = ((BinaryOperation) ((BinaryOperation) e).rhs).lhs;
            return ((BinaryOperation) e).lhs.equals(a);
        }
        return false;
    }

    private static boolean Axiom7(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Or.class) {
            Expression b = ((BinaryOperation) ((BinaryOperation) e).rhs).rhs;
            return ((BinaryOperation) e).lhs.equals(b);
        }
        return false;
    }

    private static boolean Axiom8(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&

                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Impl.class &&

                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs.getClass() == Or.class) {
            Expression a, b, c;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            c = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;
            b = ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs;

            return ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).rhs.equals(c) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs).lhs.equals(a) &&
                    ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).lhs).rhs.equals(b);
        }
        return false;
    }

    private static boolean Axiom9(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Impl.class &&
                ((BinaryOperation) e).rhs.getClass() == Impl.class &&

                ((BinaryOperation) ((BinaryOperation) e).rhs).lhs.getClass() == Impl.class &&
                ((BinaryOperation) ((BinaryOperation) e).rhs).rhs.getClass() == Not.class &&
                ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs.getClass() == Not.class) {
            Expression a, b;
            a = ((BinaryOperation) ((BinaryOperation) e).lhs).lhs;
            b = ((BinaryOperation) ((BinaryOperation) e).lhs).rhs;

            return (((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).lhs).equals(a) &&
                    ((Not) ((BinaryOperation) ((BinaryOperation) e).rhs).rhs).exp.equals(a) &&
                    ((Not) ((BinaryOperation) ((BinaryOperation) ((BinaryOperation) e).rhs).lhs).rhs).exp.equals(b);
        }
        return false;
    }

    private static boolean Axiom10(Expression e) {
        if (e.getClass() == Impl.class &&
                ((BinaryOperation) e).lhs.getClass() == Not.class &&
                ((Not) ((BinaryOperation) e).lhs).exp.getClass() == Not.class) {

            if (((Not) ((BinaryOperation) e).lhs).exp.getClass() == Not.class) {
                return ((Not) (((Not) ((BinaryOperation) e).lhs).exp)).exp.equals(((BinaryOperation) e).rhs);
            }
        }
        return false;
    }
}
