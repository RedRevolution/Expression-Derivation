import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrigoFunc implements Derivate {
    private String type;
    private String factorold;
    private String factorder;
    private BigInteger exp;

    public TrigoFunc(String s) {
        String str = s.trim();
        Pattern p = Pattern.compile("(sin|cos)\\s*\\(\\s*x\\s*\\)" +
                "(\\s*\\^\\s*[+-]?\\d+)?");
        Matcher m = p.matcher(str);
        m.find();
        type = m.group(1);
        factorold = "x";
        factorder = "1";
        if (m.group(2) != null) {
            String temp = m.group(2);
            temp = temp.trim();
            temp = temp.substring(1);
            temp = temp.trim();
            exp = new BigInteger(temp);
        } else {
            exp = BigInteger.ONE;
        }
    }

    public TrigoFunc(String ty, String factorOld, String factorDer, String ex) {
        type = ty;
        factorold = factorOld;
        factorder = factorDer;
        exp = new BigInteger(ex);
    }

    public String getExp() {
        return exp.toString();
    }

    public void mul(String s) {
        String str = s.trim();
        Pattern p = Pattern.compile("(sin|cos)\\s*\\(\\s*x\\s*\\)" +
                "(\\s*\\^\\s*[+]?\\d+)?");
        Matcher m = p.matcher(str);
        m.find();
        if (m.group(2) != null) {
            String temp = m.group(2);
            temp = temp.trim();
            temp = temp.substring(1);
            temp = temp.trim();
            BigInteger tmp = new BigInteger(temp);
            exp = exp.add(tmp);
        } else {
            exp = exp.add(BigInteger.ONE);
        }
    }

    public String derivate() {
        String ans;
        if (factorder.equals("0")) {
            return "(0)";
        }
        if (type.equals("sin")) {
            if (exp.toString().equals("1")) {
                ans = "cos(" + factorold + ")";
                if (!factorder.equals("1")) {
                    ans = ans + "*" + factorder;
                }
            } else if (exp.toString().equals("2")) {
                ans = exp.toString() + "*" + "sin(" + factorold
                        + ")" + "*cos(" + factorold + ")";
                if (!factorder.equals("1")) {
                    ans = ans + "*" + factorder;
                }
            } else {
                if (factorder.equals("1")) {
                    ans = exp.toString() + "*" + "sin(" + factorold
                            + ")^" + exp.subtract(BigInteger.ONE).toString()
                            + "*cos(" + factorold + ")";
                } else {
                    ans = exp.toString() + "*" + "sin(" + factorold + ")^" +
                            exp.subtract(BigInteger.ONE).toString() + "*cos("
                            + factorold + ")*" + factorder;
                }

            }
        } else {
            if (exp.toString().equals("1")) {
                if (factorder.equals("1")) {
                    ans = "-1*sin(" + factorold + ")";
                } else {
                    ans = "-1*sin(" + factorold + ")*" + factorder;
                }
            } else if (exp.toString().equals("2")) {
                if (factorder.equals("1")) {
                    ans = "-2*cos(" + factorold + ")"
                            + "*sin(" + factorold + ")";
                } else {
                    ans = "-2*cos(" + factorold + ")"
                            + "*sin(" + factorold + ")*" + factorder;
                }
            } else {
                if (factorder.equals("1")) {
                    ans = exp.multiply(BigInteger.valueOf(-1)).toString() + "*"
                            + "cos(" + factorold + ")^"
                            + exp.subtract(BigInteger.ONE).toString()
                            + "*sin(" + factorold + ")";
                } else {
                    ans = exp.multiply(BigInteger.valueOf(-1)).toString()
                            + "*" + "cos(" + factorold + ")^"
                            + exp.subtract(BigInteger.ONE).toString()
                            + "*sin(" + factorold + ")*" + factorder;
                }
            }
        }
        return "(" + ans + ")";
    }

    public String toString() {
        if (exp.equals("1")) {
            return "(" + type + "(" + factorold + ")" + ")";
        } else {
            return "(" + type + "(" + factorold + ")^" + exp.toString() + ")";
        }
    }
}
