import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static BufferedReader br = null;
    public static String toProve = null;
    public static String WRONG_MESSAGE = "Высказывание ложно при ";
    public static String EQUAL = "=";
    public static String DERIVATION = "|=", PROOF = "|-", LEFT_BRACKET = "(", RIGHT_BRACKET = ")", IMPLICATION = "->", COMMA = ",";
    public static HashMap<String, ArrayList<String>> allPrimitives = new HashMap<>();
    public static Integer numOfTheVars = 0;
    public static ArrayList<ArrayList<String>> proofs = new ArrayList<>();
    public static HashMap<String, Integer> allVars = new HashMap();
    public static ArrayList<String> varsName = new ArrayList<>();


    public static void main(String[] args) {

        try {
            BufferedReader in = Files.newBufferedReader(Paths.get("input.txt"), Charset.forName("UTF-8"));
            BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"));

            String string = in.readLine();
            String[] splited, hypotisis;

            splited = string.split("\\|=");
            hypotisis = splited[0].split(COMMA);

            String newHeader = string.replace(DERIVATION, PROOF);
            ArrayList<String> hypList = new ArrayList<>();

            for (String hypotisi : hypotisis) {
                if (!hypotisi.isEmpty()) {
                    hypList.add(hypotisi);
                }
            }

            String formula = splited[1];

            StringBuilder t = new StringBuilder();

            if (!hypotisis[0].equals("")) {
                for (int i = hypotisis.length - 1; i >= 0; i--) {
                    t.append(LEFT_BRACKET);
                    t.append(hypotisis[i]);
                    t.append(RIGHT_BRACKET);
                    t.append(IMPLICATION);
                }
            }

            t.append(LEFT_BRACKET);
            t.append(formula);
            t.append(RIGHT_BRACKET);


            String formulaWithHyp = t.toString();
            Expression expression = Parser.parse(formulaWithHyp);

            findAllVars(expression);
            int n = countVars(formulaWithHyp);
            //System.out.println(n);
            evaluate(expression, out);

            out.write(newHeader + "\n");
            String path = "src/primitives.txt";

            BufferedReader reader = Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8"));
            String readLine = reader.readLine();
            while (true) {
                if (readLine == null) break;
                String header1 = readLine;
                ArrayList<String> prime = new ArrayList<>();

                readLine = reader.readLine();
                while (!readLine.equals("=====")) {
                    prime.add(readLine);
                    readLine = reader.readLine();
                }
                allPrimitives.put(header1, prime);
                readLine = reader.readLine();
            }
            //System.out.println("ewrge");
            int lim = 1 << numOfTheVars;
            for (int i = 0; i < lim; i++) {
                ArrayList<String> oneProof = new ArrayList<>();

                StringBuilder header = new StringBuilder();
                for (int j = 0; j < numOfTheVars; j++) {
                    header.append(getbit(i, j) ? "" : "!").append(varsName.get(j));
                    if (j != numOfTheVars - 1) {
                        header.append(',');
                    }
                }
                header.append(PROOF);
                assert expression != null;
                header.append(expression.s);

                oneProof.add(header.toString());

                proof(expression, oneProof, i);

                proofs.add(oneProof);
            }

            for (int i = 0; i < proofs.size(); i++) {
                for (int j = 0; j < numOfTheVars; j++) {
                    proofs.set(i, z2.rebuildProof(proofs.get(i)));
                }
                proofs.get(i).remove(0);
                for (int j = 0; j < proofs.get(i).size(); j++) {
                    out.write(proofs.get(i).get(j));
                    out.write('\n');
                    out.flush();
                }
            }
            for (int i = 0; i < numOfTheVars; i++) {
                String one = varsName.get(i);
                ArrayList<String> tempList = allPrimitives.get("special_primitive");

                for (int j = 0; j < 1 << (numOfTheVars - i - 1); j++) {
                    for (String aTempList : tempList) {
                        StringBuilder ss = new StringBuilder();
                        for (int z = 0; z < aTempList.length(); z++) {
                            if (aTempList.charAt(z) == 'A') {
                                ss.append("(").append(one).append(")");
                            } else {
                                ss.append(aTempList.charAt(z));
                            }
                        }
                        out.write(ss.toString() + '\n');
                    }
                    StringBuilder expr = new StringBuilder();
                    expr.append(LEFT_BRACKET);

                    for (int k = i + 1; k < numOfTheVars; k++) {
                        Boolean bit = (j & (1 << (k - i - 1))) != 0;
                        expr.append(bit ? "" : "!");
                        expr.append(varsName.get(k));
                        expr.append(IMPLICATION);
                    }
                    expr.append(LEFT_BRACKET);
                    expr.append(formulaWithHyp);
                    expr.append(RIGHT_BRACKET);
                    expr.append(RIGHT_BRACKET);

                    out.write("(" + one + "->" + expr.toString() + ")->(!" + one + "->" + expr.toString() + ")->((" + one + "|!" + one + ")->" + expr + ")");
                    out.newLine();
                    out.write("(!" + one + "->" + expr + ")->((" + one + "|!" + one + ")->" + expr + ")");
                    out.newLine();
                    out.write("((" + one + "|!" + one + ")->" + expr + ")");
                    out.newLine();
                    out.write(expr.toString());
                    out.newLine();
                }
            }
            for (String hyp : hypList) {
                out.write(hyp + "\n");
            }

            Expression formExpr = Parser.parse(formulaWithHyp);
            for (int i = 0; i < hypList.size(); i++) {
                assert formExpr != null;
                formExpr = ((BinaryOperation) formExpr).rhs;
                out.write(formExpr.s + '\n');
                //System.out.println(formExpr.toString());
            }

            out.flush();
            out.close();
            out.flush();
            out.close();
        } catch (IOException ignored) {
        }
    }

    static void findAllVars(Expression expression) {
        if (expression instanceof Const) {
            if (!allVars.containsKey(expression.s)) {
                allVars.put(expression.s, numOfTheVars);
                varsName.add(expression.s);
                numOfTheVars++;
            }
        } else {
            if (expression instanceof Not) {
                findAllVars(((Not) expression).exp);
            } else {
                findAllVars(((BinaryOperation) expression).lhs);
                findAllVars(((BinaryOperation) expression).rhs);
            }
        }
    }

    static void evaluate(Expression expression, BufferedWriter out) throws IOException {

        int lim = (int) Math.pow(2, numOfTheVars);

        for (int i = 0; i < lim; ++i) {
            if (!expression.evaluate(i, allVars)) {
                wrong(i, out);
            }
        }
    }

    static void wrong(int mask, BufferedWriter out) throws IOException {
        StringBuilder temporary = new StringBuilder();
        temporary.append(WRONG_MESSAGE);

        int j = 0;
        while (j < numOfTheVars) {
            temporary.append((varsName.get(j)));
            temporary.append(EQUAL);
            if ((((mask >> j) & 1) == 1)) {
                temporary.append("И");
            } else {
                temporary.append("Л");
            }

            if (j != numOfTheVars - 1)
                temporary.append(COMMA);
            temporary.append(" ");
            j++;
        }

        out.write(temporary.toString());
        out.flush();

        out.close();
        System.exit(0);
    }

    static int countVars(String s) {
        int[] a = new int[26];
        char ch;


        for (int i = 0; i < s.length(); ++i) {
            ch = s.charAt(i);
            if (Character.isAlphabetic(ch))
                a[ch - 'A']++;
        }

        int ans = 0;

        for (int i = 0; i < 26; ++i) {
            if (a[i] != 0)
                ++ans;
        }

        return ans;
    }

    static int setbit(int x, int i) {
        return x | (1 << i);
    }

    static int resbit(int x, int i) {
        return setbit(x, i) ^ (1 << i);
    }

    static boolean getbit(int x, int i) {
        return (x & (1 << i)) != 0;
    }

    private static void proof(Expression expression, ArrayList<String> singleProof, int mask) {
        if (expression instanceof Not) {
            proof(((Not) expression).exp, singleProof, mask);

            String key = (((Not) expression).exp.proved(mask, allVars) ? 1 : 0) + expression.whatType();

            ArrayList<String> template = new ArrayList<>(allPrimitives.get(key));

            for (String aTemplate : template) {
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < aTemplate.length(); j++) {
                    if (aTemplate.charAt(j) == 'A') {
                        result.append(LEFT_BRACKET);
                        result.append(((Not) expression).exp.s);
                        result.append(RIGHT_BRACKET);
                    } else {
                        result.append(aTemplate.charAt(j));
                    }
                }
                singleProof.add(result.toString());
            }
        } else if (expression instanceof Const) {
            if (getbit(mask, allVars.get(expression.s))) {
                singleProof.add(expression.s);
            } else {
                singleProof.add("!" + expression.s);
            }
        } else if (expression instanceof BinaryOperation) {
            BinaryOperation binExpr = (BinaryOperation) expression;
            //System.out.println(allPrimitives);
            proof(((BinaryOperation) expression).lhs, singleProof, mask);
            //System.out.println(expression.getRight().toString());
            proof(((BinaryOperation) expression).rhs, singleProof, mask);
            String key = ((((BinaryOperation) expression).lhs.proved(mask, allVars)) ? 1 : 0) + expression.whatType() + ((((BinaryOperation) expression).rhs.proved(mask, allVars)) ? 1 : 0);
            //System.out.println(allPrimitives.get(key));
            ArrayList<String> template = new ArrayList<>(allPrimitives.get(key));

            for (String aTemplate : template) {
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < aTemplate.length(); j++) {
                    if (aTemplate.charAt(j) == 'A') {
                        result.append(((BinaryOperation) expression).lhs.s);
                    } else if (aTemplate.charAt(j) == 'B') {
                        result.append(((BinaryOperation) expression).rhs.s);
                    } else {
                        result.append(aTemplate.charAt(j));
                    }
                }
                singleProof.add(result.toString());
            }
        }
    }

}
