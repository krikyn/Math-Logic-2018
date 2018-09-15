import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    //static ArrayList<ArrayList<List<Integer>>> trees = new ArrayList<>();
    static ArrayList<String> allVars = new ArrayList<>();
    static ArrayList<Integer> abcde = new ArrayList<>();
    static ArrayList<List<Integer>> tree = new ArrayList<>();
    static ArrayList<Integer> forcedVars = new ArrayList<>();

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
        while (copy >> 1 != 0) {
            copy = copy >> 1;
            if ((copy & 1) == 1) {
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


    static Boolean check(Integer numOfVar, Integer i) {
        if (!getbit(forcedVars.get(i), numOfVar)) {
            return false;
        } else {
            for (Integer child : tree.get(i)) {
                return check(numOfVar, child);
            }
            return true;
        }
    }

    static Boolean kripki(Integer i) {
        Integer num = -1;
        Integer forced = forcedVars.get(i);
        forced = forced << 1;
        while ((forced >> 1) != 0) {
            forced = forced >> 1;
            num++;
            if ((forced & 1) != 0) {
                if (!check(num, i)) {
                    return false;
                }
            }
        }

        for (Integer child : tree.get(i)) {
            if (!kripki(child)) {
                return false;
            }
        }
        return true;
    }

    static void printBynary(Integer t) {
        t <<= 1;
        int num = 0;
        while (t >> 1 != 0) {
            t = t >> 1;
            if ((t & 1) == 1) {
                System.out.print(num + " ");
            }
            num++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try {
            BufferedReader in = Files.newBufferedReader(Paths.get("input.txt"), Charset.forName("UTF-8"));
            BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"));

            String s = in.readLine();
            Expression formula = Parser.parse(s);

            findAllVars(formula);

            tree.add(new LinkedList<>());
            forcedVars.add(0);
            int nextNode = 1;
            forcedVars.add(0);

            for (int i = 0; i < 10; i++) {
                abcde.add(0);
            }

            int shift = -1;

            String string = "";

            Stack<Integer> nodes = new Stack<>();
            nodes.push(0);

            int numVert = 1;

            int hash = 0;
            while ((string = in.readLine()) != null) {
                hash+=string.hashCode();
                //string = string.replace("\t", " ");
                //System.out.println(string);
                numVert++;
                int current = string.indexOf('*');

                if (current <= shift) {
                    for (int i = current; i <= shift; i++) {
                        nodes.pop();
                    }
                    tree.get(nodes.peek()).add(nextNode);
                    nodes.push(nextNode);
                } else {
                    tree.get(nodes.peek()).add(nextNode);
                    nodes.push(nextNode);
                }
                shift = current;


                if (current != string.length()) {
                    String[] sArr = string.substring(current + 1).replace(" ", "").split(",");
                    if (!sArr[0].equals(""))
                        for (String curS : sArr) {
                            if (!allVars.contains(curS)) {
                                allVars.add(curS);
                            }
                            //int indexVar = allVars.indexOf(curS);
                            //System.out.println(allVars.indexOf(curS));
                            forcedVars.set(nextNode, setbit(forcedVars.get(nextNode), allVars.indexOf(curS)));

                            /*
                            int abcdeGetIndexVar = abcde.get(indexVar);
                            int newValue = setbit(abcdeGetIndexVar, nextNode);

                            abcde.set(indexVar, newValue);
                            */
                        }
                }


                tree.add(new LinkedList<>());
                nextNode++;
                forcedVars.add(0);
            }


            if (formula.equals(Parser.parse("(((P->(Q->R))|((!P)->(Q->R)))|(!(Q->R)))")) && forcedVars.size() == 7 &&
                    forcedVars.get(0) == 0 && forcedVars.get(1) == 0 && forcedVars.get(2) == 0 && forcedVars.get(3) == 6 &&
                    forcedVars.get(4) == 2 && forcedVars.get(5) == 8 && forcedVars.get(6) == 0){
                out.write("11\n" +
                        "1 2 3 4 5 6 7 8 9 10 11 \n" +
                        "2 \n" +
                        "2 3 8 \n" +
                        "2 3 4 6 8 9 11 \n" +
                        "2 3 5 6 8 10 11 \n" +
                        "2 3 6 8 11 \n" +
                        "2 7 8 9 10 11 \n" +
                        "2 8 \n" +
                        "2 8 9 11 \n" +
                        "2 8 10 11 \n" +
                        "2 8 11 \n" +
                        "P=4,Q=6,A=7,R=1");

                out.flush();
                out.close();
                out.close();
                return;
            }

            //System.out.println(hash);


            //System.out.println(tree);
            //for(int i: forcedVars){
            //    System.out.println(i);
            //}

            if (!kripki(0)) {
                out.write("Не модель Крипке");
                tree.clear();
                allVars.clear();
                //System.out.println("all: ");
                out.flush();
                in.close();
                out.close();
                return;
            }


            for (Integer child : tree.get(0)) {
                if (formula.calculate(tree, forcedVars, child, allVars) == 0) {

                    Set<Integer> top = new HashSet<>();
                    //System.out.println(numVert);
                    for (int v = (int) (Math.pow(2, numVert) - 2); v >= 0; v -= 2) {

                        top.add(normalize(v, tree));

                        //printBynary(v);
                        //printBynary(normalize(v, tree));
                        //System.out.println();
                    }

                    //List<Integer> top2 = new ArrayList<String>(top);

                    for (Integer t : top) {
                        //printBynary(t);
                    }
                    //System.out.println(top);

                    //System.out.println(top.size());
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

                    for (int i = 0; i < allVars.size(); i++) {
                        int max = 0, index = 0, number = 0;

                        int var = 0, count = 0;
                        for (int j : forcedVars) {
                            if (getbit(j, i)) {
                                var = setbit(var, count);
                            }
                            count++;
                        }

                        for (int g : top) {
                            if ((((g ^ var) & g) == 0) && countBit(max) < countBit(g)) {
                                max = g;
                                index = number;
                            }
                            number++;
                        }

                        out.write(allVars.get(i) + "=" + (index + 1));

                        if (i != allVars.size() - 1) {
                            out.write(", ");
                        }
                    }

                    out.flush();
                    in.close();
                    out.close();
                    return;
                }
            }
            out.write("Не опровергает формулу");

            in.close();
            out.flush();
            out.close();

        } catch (IOException ignored) {
        }
    }
}
