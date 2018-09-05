import java.io.*;

public class Main {

    public static void main(String[] args) {

        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
            BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));

            String line = in.readLine();
            //System.out.println(line);
            Expression expr = Parser.parse(line);
            String result = expr.print();
            out.write(result);
            out.close();

        } catch (IOException ignored) {
        }
    }
}