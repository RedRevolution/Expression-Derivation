import java.math.BigInteger;

public class ConstantFunc implements Derivate {
    private BigInteger num;

    public ConstantFunc(String str) {
        num = new BigInteger(str);
    }

    public void mul(String str) {
        num = num.multiply(new BigInteger(str));
    }

    public String derivate() {
        return "(0)";
    }

    public String toString() {
        return "(" + num.toString() + ")";
    }
}
