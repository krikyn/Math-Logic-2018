public class Parser {
    public static Expression parse(String s) {

        if (s.length() == 0)
            return null;

        s = reduceBrackets(s);

        for (int i = 0; i < s.length() - 2; ++i) {

            if (s.charAt(i) == '(')
                i += skipBrackets(s.substring(i));
            if (i >= s.length())
                break;

            if (s.charAt(i) == '-' && s.charAt(i + 1) == '>') {
                String s1 = reduceBrackets(s.substring(0, i));
                String s2 = reduceBrackets(s.substring(i + 2));
                Expression e1 = parse(s1);
                Expression e2 = parse(s2);
                return new Impl(
                        e1,
                        e2,
                        "(" + e1.s + "->" + e2.s + ")"
                );
            }
        }

        for (int i = s.length() - 1; i > 0; --i) {
            if (s.charAt(i) == ')')
                i -= skipBracketsLeft(s.substring(0, i + 1));
            if (i <= 0)
                break;

            if (s.charAt(i) == '|') {
                String s1 = reduceBrackets(s.substring(0, i));
                String s2 = reduceBrackets(s.substring(i + 1));
                Expression e1 = parse(s1);
                Expression e2 = parse(s2);
                return new Or(
                        e1,
                        e2,
                        "(" + e1.s + "|" + e2.s + ")"
                );
            }
        }

        for (int i = s.length() - 1; i > 0; --i) {
            if (s.charAt(i) == ')')
                i -= skipBracketsLeft(s.substring(0, i + 1));
            if (i <= 0)
                break;

            if (s.charAt(i) == '&') {
                String s1 = reduceBrackets(s.substring(0, i));
                String s2 = reduceBrackets(s.substring(i + 1));
                Expression e1 = parse(s1);
                Expression e2 = parse(s2);
                return new And(
                        e1,
                        e2,
                        "(" + e1.s + "&" + e2.s + ")"
                );
            }
        }

        if (s.charAt(0) == '!') {
            String s1 = reduceBrackets(s.substring(1));
            Expression e1 = parse(s1);
            return new Not(e1, "!(" + e1.s + ")");
        }

        return new Const(reduceBrackets(s));
    }


    private static String reduceBrackets(String s) {
        int i = 0;
        int depth = 0;
        if (s.charAt(0) != '(') return s;

        do {
            if (s.charAt(i) == '(') {
                ++depth;
            }
            if (s.charAt(i) == ')') {
                --depth;
            }
            i++;
        } while (depth > 0 && i < s.length());

        if (depth == 0 && i == s.length()) {
            return reduceBrackets(s.substring(1, s.length() - 1));
        } else {
            return s;
        }
    }

    private static int skipBrackets(String s) {
        int depth = 1;
        int i = 1;

        if (i >= s.length()) return 0;

        do {
            if (s.charAt(i) == '(') {
                ++depth;
            }
            if (s.charAt(i) == ')') {
                --depth;
            }
            i++;
        } while (depth > 0);
        return i;
    }

    private static int skipBracketsLeft(String s) {
        int depth = 0;
        int i = s.length() - 1;

        if (s.length() == 1) return 0;

        try {
            do {
                if (s.charAt(i) == '(') {
                    ++depth;
                }
                if (s.charAt(i) == ')') {
                    --depth;
                }
                --i;
            } while (depth < 0);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(s);
        }
        return s.length() - i - 1;
    }
}
