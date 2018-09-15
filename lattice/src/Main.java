import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static ArrayList<Integer>[] graph = new ArrayList[200];
    static int[][] map = new int[200][200];
    static int[][] sum = new int[200][200];
    static int[][] mul = new int[200][200];
    static boolean[] visited = new boolean[200];

    static int n;
    static int maximum;
    static int minimum;

    static void dfs(int i, int from) {
        visited[i] = true;
        for (int next : graph[i]) {
            if (!visited[next]) {
                map[from][next] = 1;
                dfs(next, from);
            }
        }
    }

    static void checkSumAndMul() throws Exception {
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                checkSum(a, b);
            }
        }
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                checkMul(a, b);
            }
        }
    }

    static void checkSum(int a, int b) throws Exception {
        ArrayList<Integer> candidates = new ArrayList<>();
        int min_c = -1;

        for (int c = 0; c < n; c++) {
            if (map[a][c] == 1 && map[b][c] == 1) {
                candidates.add(c);
                /*if (min_c == -1) {
                    min_c = c;
                } else {
                    min_c = -1;
                    break;
                    if (map[c][min_c] == 1) {
                        min_c = c;
                    }
                }*/
            }
        }

        for (int i = 0; i < candidates.size(); i++) {
            int counter = 0;
            for (int j = 0; j < candidates.size(); j++) {
                if (map[candidates.get(i)][candidates.get(j)] == 1) {
                    counter++;
                }
            }

            if (counter == candidates.size()) {
                min_c = candidates.get(i);
            }
        }


        if (a != b && min_c == -1) {
            try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                out.write("Операция '+' не определена: " + (a + 1) + "+" + (b + 1));
            }
            System.exit(0);
        }

        sum[a][b] = min_c;
    }

    static void checkMul(int a, int b) throws Exception {
        ArrayList<Integer> candidates = new ArrayList<>();
        int max_c = -1;

        for (int c = 0; c < n; c++) {
            if (map[c][a] == 1 && map[c][b] == 1) {
                candidates.add(c);
                /*if (max_c == -1) {
                    max_c = c;
                } else {
                    max_c = -1;
                    break;
                    if (map[max_c][c] == 1) {
                        max_c = c;
                    }
                }*/
            }
        }

        for (int i = 0; i < candidates.size(); i++) {
            int counter = 0;
            for (int j = 0; j < candidates.size(); j++) {
                if (map[candidates.get(j)][candidates.get(i)] == 1) {
                    counter++;
                }
            }

            if (counter == candidates.size()) {
                max_c = candidates.get(i);
            }
        }

        if (a != b && max_c == -1) {
            try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                out.write("Операция '*' не определена: " + (a + 1) + "*" + (b + 1));
            }
            System.exit(0);
        }

        mul[a][b] = max_c;
    }

    static void checkDistr() throws Exception {
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                for (int c = 0; c < n; c++) {
                    if (a != b && a != c && b != c) {
                        if (mul[a][sum[b][c]] != sum[mul[a][b]][mul[a][c]]) {
                            try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                                out.write("Нарушается дистрибутивность: " + (a + 1) + "*(" + (b + 1) + "+" + (c + 1) + ")");
                            }
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }

    static int implication(int a, int b) {
        int max_c = -1;

        for (int c = 0; c < n; c++) {
            int m = mul[a][c];

            if (map[m][b] == 1) {
                if (max_c == -1) {
                    max_c = c;
                } else {
                    if (map[max_c][c] == 1) {
                        max_c = c;
                    }
                }
            }
        }
        return max_c;
    }


    public static void main(String[] args) throws Exception {

        for (int i = 0; i < graph.length; i++)
            graph[i] = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("input.txt"))) {

            n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                String line = scanner.nextLine();
                ArrayList<Integer> a = Arrays.stream(line.split(" ")).map(e -> (Integer.parseInt(e) - 1)).collect(Collectors.toCollection(ArrayList::new));
                graph[i] = a;
            }

            for (int i = 0; i < n; i++) {

                map[i][i] = 1;

                for (int j = 0; j < n; j++) {
                    visited[j] = false;
                }

                dfs(i, i);
            }

            /*for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print((map[i][j] + 0) + " ");
                }
                System.out.println();
            }*/

            checkSumAndMul();

            checkDistr();

            //CHECK BULEV
            //find 0 and 1
            /*for (int candidate = 0; candidate < n; candidate++) {
                int out = 0;
                int in = 0;
                for (int to = 0; to < n; to++) {
                    if (map[candidate][to] == 1) {
                        out++;
                    }
                    if (map[to][candidate] == 1) {
                        in++;
                    }
                }
                if (out == n) {
                    minimum = candidate;
                }
                if (in == n) {
                    maximum = candidate;
                }
            }*/

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && implication(i, j) == -1) {
                        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                            out.write("Операция '->' не определена: " + (i + 1) + "->" + (j + 1));
                        }
                        System.exit(0);
                    }
                }
            }


            minimum = 0;
            maximum = 0;
            for (int i = 0; i < n; i++) {
                if (map[i][minimum] == 1) {
                    minimum = i;
                }
                if (map[maximum][i] == 1) {
                    maximum = i;
                }
            }

            /*for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i != j && implication(i, j) == -1) {
                        try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                            out.write("Операция '->' не определена: " + i + "->" + j);
                        }
                        System.exit(0);
                    }
                }
            }*/


            //System.out.println(maximum + "-" + minimum);
            for (int A = 0; A < n; A++) {
                int notA = implication(A, minimum);
                if (sum[A][notA] != maximum || mul[A][notA] != minimum) {
                    try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                        out.write("Не булева алгебра: " + (A + 1) + "+~" + (A + 1));
                    }
                    System.exit(0);
                }
            }

            try (BufferedWriter out = Files.newBufferedWriter(Paths.get("output.txt"), Charset.forName("UTF-8"))) {
                out.write("Булева алгебра");
            }
            System.exit(0);
        }
    }
}
