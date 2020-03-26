import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerFunc implements Derivate {
    private BigInteger exp;

    public PowerFunc(String str) {
        String temp = str.trim();
        if (temp.matches("x")) {
            exp = BigInteger.ONE;
        } else {
            Pattern p = Pattern.compile("x\\s*\\^\\s*([+-]?\\d+)");
            Matcher m = p.matcher(temp);
            m.find();
            exp = new BigInteger(m.group(1));
        }
    }

    public void mul(String str) {
        String temp = str.trim();
        if (temp.matches("x")) {
            exp = exp.add(BigInteger.ONE);
        } else {
            Pattern p = Pattern.compile("x\\s*\\^\\s*([+-]?\\d+)");
            Matcher m = p.matcher(temp);
            m.find();
            BigInteger tmp = new BigInteger(m.group(1));
            exp = exp.add(tmp);
        }
    }

    public String derivate() {
        if (exp.equals(BigInteger.ONE)) {
            return "1";
        } else if (exp.equals(BigInteger.valueOf(2))) {
            return "(2*x)";
        } else {
            return "(" + exp.toString() + "*x^" +
                    exp.subtract(BigInteger.ONE).toString() + ")";
        }
    }

    public String toString() {
        if (exp.equals(BigInteger.ONE)) {
            return "(x)";
        }
        else {
            return "(x^" + exp.toString() + ")";
        }
    }

}
