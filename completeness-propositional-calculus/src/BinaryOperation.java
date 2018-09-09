import java.util.ArrayList;


public abstract class BinaryOperation extends Expression {
    public Expression lhs;
    public Expression rhs;

    public BinaryOperation(Expression l, Expression r) {
        this.lhs = l;
        this.rhs = r;
    }

    public ArrayList<Expression> getProof(int mask, int kth, int total) {
        ArrayList<Expression> res;

        if (kth == -1) {           // зафиксировали все - просим построиться от маски
            //return makeMe(mask, total);
        }

        ArrayList<Expression> der1, der2;                               // два вывода от разных масок
        der1 = this.getProof(setbit(mask, kth), kth - 1, total);
        der2 = this.getProof(resbit(mask, kth), kth - 1, total);


        res = Terminator.terminate(der1, der2, mask, kth, total);        // поглоим переменную
        return res;
    }

    /**
     * Здесь мы строим вывод связки this по даному предположеню mask.
     * в итоге связка может получиться true или false, что будет отражено в выводе но, вообще-то, роли не играет.
     * сначала будут построены выводы для сыновей, а потом в зависимости значений сыноыей
     * на этой маске(вывод их тоже зависит от маски, так что роли по сути не играет), будет проведен вывод this или (!this)
     *
     * @param mask  маска
     * @param total сколько всего есть переменных (сколько битов из маски нам нужны)
     * @return вывод сыноыей, потом вывод нас самих
     */
    /*public ArrayList<Expression> makeMe(int mask, int total) {

        ArrayList<Expression> res = new ArrayList<Expression>();

        res.addAll(lhs.makeMe(mask, total));
        res.addAll(rhs.makeMe(mask, total));


        String a, b;
        boolean l = lhs.evaluate(mask);
        boolean r = rhs.evaluate(mask);

        // строки, которые будут как А и Б в элементарных связках
        a = lhs.s;
        b = rhs.s;


        if (l) {
            if (r)
                res.addAll(A_B(a, b));
            else
                res.addAll(A_nB(a, b));
        } else {
            if (r)
                res.addAll(nA_B(a, b));
            else
                res.addAll(nA_nB(a, b));
        }


        return res;
    }*/

    public abstract ArrayList<Expression> A_B(String a, String b);

    public abstract ArrayList<Expression> A_nB(String a, String b);

    public abstract ArrayList<Expression> nA_B(String a, String b);

    public abstract ArrayList<Expression> nA_nB(String a, String b);
}

