import java.util.ArrayList;

public class Replacer {
    public static ArrayList<Expression> replase(String[] ss, String a, String b) {
        if (a == null || b == null) {
            System.out.println("Fuck!! 2 args");
        }
        String buf;
        ArrayList<Expression> res = new ArrayList<Expression>();
        for (String s : ss) {
            buf = s.replace("@", a);
            buf = buf.replace("#", b);
            res.add(Parser.parse(buf));
        }
        return res;
    }

    public static ArrayList<Expression> replase(String[] ss, String a) {

        if (a == null) {
            System.out.println("Fuck!! 1");
        }
        String buf;
        ArrayList<Expression> res = new ArrayList<Expression>();
        for (String s : ss) {
            buf = s.replace("@", a);
            res.add(Parser.parse(buf));
        }
        return res;
    }
}
