import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    static HashMap<Expression, Integer> lines;
    static HashMap<Expression, ArrayList> mpParts = new HashMap<>(53000, 1);
    static HashMap<Expression, Integer> assumptions = new HashMap<>(53000, 1);

    public static void main(String[] args) {
        lines = new HashMap<>(53000, 1);

        try {
            BufferedReader in = Files.newBufferedReader(Paths.get("input.txt"), Charset.forName("UTF-8"));
            BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"));

            String[] header = in.readLine().replace(" ", "").split("\\|-");
            if (!header[0].isEmpty()) {
                String[] dataStrings = header[0].split(",");
                for (int i = 0; i < dataStrings.length; i++) {
                    assumptions.put(Parser.parse(dataStrings[i]), i);
                }
            }

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

                out.write("(" + curNum + ") " + line + " (");

                boolean hintIsWriten = false;

                Integer assumpt = assumptions.get(expr);
                if (assumpt != null) {
                    out.write("Предп. " + (assumpt + 1));
                    hintIsWriten = true;
                }

                if (!hintIsWriten) {
                    if (Axiom1(expr)) {
                        out.write("Сх. акс. 1");
                        hintIsWriten = true;
                    } else if (Axiom2(expr)) {
                        out.write("Сх. акс. 2");
                        hintIsWriten = true;
                    } else if (Axiom3(expr)) {
                        out.write("Сх. акс. 3");
                        hintIsWriten = true;
                    } else if (Axiom4(expr)) {
                        out.write("Сх. акс. 4");
                        hintIsWriten = true;
                    } else if (Axiom5(expr)) {
                        out.write("Сх. акс. 5");
                        hintIsWriten = true;
                    } else if (Axiom6(expr)) {
                        out.write("Сх. акс. 6");
                        hintIsWriten = true;
                    } else if (Axiom7(expr)) {
                        out.write("Сх. акс. 7");
                        hintIsWriten = true;
                    } else if (Axiom8(expr)) {
                        out.write("Сх. акс. 8");
                        hintIsWriten = true;
                    } else if (Axiom9(expr)) {
                        out.write("Сх. акс. 9");
                        hintIsWriten = true;
                    } else if (Axiom10(expr)) {
                        out.write("Сх. акс. 10");
                        hintIsWriten = true;
                    }
                }

                if (!hintIsWriten){
                    ArrayList<Expression> element = mpParts.get(expr);
                    if (element != null) {
                        Integer element2 = lines.get(element.get(1));
                        if (element2 != null) {
                            out.write("M.P. " + lines.get(element.get(0)) + ", " + element2);
                            hintIsWriten = true;
                        }
                    }
                }

                /*for (int i = 0; !hintIsWriten && i < lines.size(); i++) {
                    if (lines.get(i) instanceof BinaryOperation && ((BinaryOperation) lines.get(i)).rhs.equals(expr)) {
                        for (int j = 0; !hintIsWriten && j < lines.size(); j++) {
                            if (lines.get(j).equals(((BinaryOperation) lines.get(i)).lhs)) {
                                out.write("M.P. " + (i + 1) + ", " + (j + 1));
                                hintIsWriten = true;
                            }
                        }
                    }
                }*/

                if (!hintIsWriten) {
                    out.write("Не доказано");
                }

                out.write(")");
                out.newLine();

                lines.put(expr, curNum);
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
