public class Not extends Expression {
    public Expression exp;

    Not(Expression e, String str) {
        exp = e;
        hash = str.hashCode();
        s = str;
    }

    String print(){
        return "(!" + exp.print() + ")";
    }
}
