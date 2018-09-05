public class And extends BinaryOperation {
    And(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(&," + lhs.print() + "," + rhs.print() + ")";
    }
}
