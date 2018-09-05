public class Const extends Expression {
    Const(String str) {
        s = str;
        hash = str.hashCode();
    }

    String print(){
        return s;
    }
}
