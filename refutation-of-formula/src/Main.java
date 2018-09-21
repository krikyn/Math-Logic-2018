import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static ArrayList<ArrayList<List<Integer>>> trees = new ArrayList<>();
    static ArrayList<String> allVars = new ArrayList<>();

    static void generateTree(ArrayList<String> sk, int size) {
        for (int t = 0; t < sk.size(); t++) {
            ArrayList<List<Integer>> tree = new ArrayList<>();

            for (int i = 0; i < size + 1; i++) {
                tree.add(new LinkedList<>());
            }

            int nextNode = 1;
            Stack<Integer> nodes = new Stack<>();
            nodes.push(0);

            String s = sk.get(t);
            for (int c = 0; c < s.length(); c++) {
                if (s.charAt(c) == '(') {
                    tree.get(nodes.peek()).add(nextNode);
                    nodes.push(nextNode);
                    nextNode++;
                } else {
                    nodes.pop();
                }
            }

            trees.add(tree);
            //System.out.println(s);
            /*if (trees.size() == 4){
                System.out.println(sk.get(t));
            }*/
        }
    }

    static int setbit(int x, int i) {
        return x | (1 << i);
    }

    static boolean getbit(int x, int i) {
        return (x & (1 << i)) != 0;
    }

    static int normalize(int n, ArrayList<List<Integer>> tree) {

        int copy = n;
        copy = copy << 1;
        int num = 0;
        while(copy>>1 !=0){
            copy = copy >> 1;
            if ((copy&1) == 1){
                n = normirovat(n, tree, num);
            }
            num++;
        }

        return n;
    }

    static int normirovat(int n, ArrayList<List<Integer>> tree, int v) {

        Stack<Integer> stack = new Stack<>();
        stack.push(v);

        while (!stack.isEmpty()) {
            int cur = stack.pop();

            n = setbit(n, cur);

            for (int i : tree.get(cur)) {
                stack.push(i);
            }
        }

        return n;
    }

    static String next(String s) {
        int n = s.length();
        String ans = "No solution";
        for (int i = n - 1, depth = 0; i >= 0; --i) {
            if (s.charAt(i) == '(')
                --depth;
            else
                ++depth;
            if (s.charAt(i) == '(' && depth > 0) {
                --depth;
                int open = (n - i - 1 - depth) / 2;
                int close = n - i - 1 - open;
                StringBuilder o = new StringBuilder(), c = new StringBuilder();
                for (int h = 0; h < open; h++) o.append('(');
                for (int h = 0; h < close; h++) o.append(')');
                ans = s.substring(0, i) + ')' + o + c;
                break;
            }
        }
        return ans;
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

    static int countBit(int n) {
        int count = 0;
        for (; n > 0; count++)
            n &= (n - 1);
        return count;
    }

    static void findAllVars(Expression expression) {
        if (expression instanceof Const) {
            if (!allVars.contains(expression.toString())) {
                allVars.add(expression.s);
            }
        } else {
            if (expression instanceof Not) {
                findAllVars(((Not) expression).exp);
            } else {
                findAllVars(((BinaryOperation) expression).rhs);
                findAllVars(((BinaryOperation) expression).lhs);
            }
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader in = Files.newBufferedReader(Paths.get("input.txt"), Charset.forName("UTF-8"));
            BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"));

            String s = in.readLine();

            Expression formula = Parser.parse(s);

            findAllVars(formula);
            int varsNumber = allVars.size();

            for (int numVert = 0; numVert <= 4; numVert++) {

                StringBuilder p = new StringBuilder();
                for (int repeat = 0; repeat < numVert; repeat++) {
                    p.append('(');
                }
                for (int repeat = 0; repeat < numVert; repeat++) {
                    p.append(')');
                }
                String pos = p.toString();

                ArrayList<String> skobki = new ArrayList<>();
                while (!pos.equals("No solution")) {
                    //System.out.println(numVert + ":" + pos);
                    skobki.add(pos);
                    pos = next(pos);
                }
                int maxLevel = numVert;
                trees.clear();
                generateTree(skobki, maxLevel);


                for (int world = 0; world < trees.size(); world++) {

                    HashSet<String> set = new HashSet<>();

                    for (int A = 0; A < (Math.pow(2, maxLevel+1) - 1); A++) {
                        for (int B = 0; B < (Math.pow(2, maxLevel+1) - 1); B++) {
                            for (int C = 0; C < (Math.pow(2, maxLevel+1) - 1); C++) {

                                int a = normalize(A, trees.get(world));
                                int b = normalize(B, trees.get(world));
                                int c = normalize(C, trees.get(world));

                                String stringSet = Integer.toString(a) + Integer.toString(b) + Integer.toString(c);

                                if (set.contains(stringSet)) {
                                    continue;
                                }
                                set.add(stringSet);

                                //for (int i = 0; i < maxLevel; i++) {
                                for (int i = 0; i < maxLevel; i++) {
                                    int result = formula.calculate(trees.get(world), a, b, c, i, allVars);

                                    if (result == 0) {
                                        //System.out.println(trees.get(world));
                                        //System.out.println(world + "," + a + "," + b + "," + c + "," + i + " - " + maxLevel);
                                        Set<Integer> top = new HashSet<>();
                                        //System.out.println("level - " + maxLevel);
                                        //System.out.println((Math.pow(2, maxLevel+1) - 1));
                                        for (int v = (int) (Math.pow(2, maxLevel+1) - 1); v >= 0; v--) {
                                            top.add(normalize(v, trees.get(world)));
                                        }

                                        System.out.println(top);
                                        /////
                                        out.write(Integer.toString(top.size()));
                                        out.newLine();

                                        ArrayList<List<Integer>> graph = new ArrayList<>();
                                        int topSize = top.size();

                                        for (int j = 0; j < topSize; j++) {
                                            graph.add(new LinkedList<>());
                                        }

                                        int xx = 0, yy = 0;
                                        for (int x : top) {
                                            for (int y : top) {
                                                if (((x ^ y) & x) == 0) {
                                                    graph.get(xx).add(yy);
                                                    //System.out.println(xx + "->" + yy);
                                                    out.write((yy + 1) + " ");
                                                    //out.newLine();
                                                }
                                                yy++;
                                            }
                                            out.newLine();
                                            xx++;
                                            yy = 0;
                                        }

                                        ArrayList<Integer> var = new ArrayList<>();
                                        var.add(a);
                                        var.add(b);
                                        var.add(c);


                                        for (int j = 0; j < varsNumber; j++) {
                                            int max = 0, index = 0, number = 0;

                                            for (int g : top) {
                                                if ((((g ^ var.get(j)) & g) == 0) && countBit(max) < countBit(g)) {
                                                    max = g;
                                                    index = number;
                                                }
                                                number++;
                                            }

                                            out.write(allVars.get(j) + "=" + (index + 1));

                                            if (j != varsNumber - 1) {
                                                out.write(", ");
                                            }
                                        }

                                        /////
                                        /*int setA = 0, setB = 0, setC = 0;
                                        int number = 0;
                                        int varA = 0, varB = 0, varC = 0;
                                        for (int g : top) {
                                            if ((((g ^ a) & g) == 0) && countBit(setA) < countBit(g)) {
                                                setA = g;
                                                varA = number;
                                            }
                                            if ((((g ^ b) & g) == 0) && countBit(setB) < countBit(g)) {
                                                setB = g;
                                                varB = number;
                                            }
                                            if ((((g ^ c) & g) == 0) && countBit(setC) < countBit(g)) {
                                                setC = g;
                                                varC = number;
                                            }
                                            number++;
                                        }

                                        out.write(allVars.get(0) + "=" + (varA + 1));

                                        if (varsNumber > 1) {
                                            out.write(", " + allVars.get(1) + "=" + (varB + 1));
                                        }

                                        if (varsNumber > 2) {
                                            out.write(", " + allVars.get(2) + "=" + (varC + 1));
                                        }*/

                                        in.close();
                                        out.close();
                                        return;

                                        //out.newLine();
                                        //out.newLine();
                                    }
                                }
                            }
                        }
                    }

                    //System.out.println("num sets " + numSets);

                }
            }

            out.write("Формула общезначима");

            in.close();
            out.close();

        } catch (IOException ignored) {
        }
    }
}
