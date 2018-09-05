public class Or extends BinaryOperation {
    Or(Expression l, Expression r, String str) {
        super(l, r);
        s = str;
        hash = str.hashCode();
    }

    String print() {
        return "(|," + lhs.print() + "," + rhs.print() + ")";
    }
}
