public class Utils {
    public static int setbit(int x, int i) {
        return x | (1 << i);
    }

    public static int getbit(int x, int i) {
        if ((x & (1 << i)) != 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
