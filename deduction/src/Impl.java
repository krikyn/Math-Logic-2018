public class Impl extends BinaryOperation {
    public Impl(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(" + lhs.print() + "->" + rhs.print() + ")";
    }
}
