public abstract class BinaryOperation extends Expression {
    public Expression lhs;
    public Expression rhs;

    public BinaryOperation(Expression l, Expression r) {
        this.lhs = l;
        this.rhs = r;
    }

}

