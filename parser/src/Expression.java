public abstract class Expression {
    public String s;
    public int hash;

    public boolean equals(Expression other)
    {
        if(this.hash==other.hash &&
                this.s.equals(other.s) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    String print(){
        return s;
    }
}
