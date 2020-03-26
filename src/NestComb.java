import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NestComb implements Derivate {
    private boolean valid;
    private String type;
    private String factorOld;
    private String factorDer;
    private String exp;
    private String expression;

    public NestComb(String str) {
        valid = checkValid(str);
        expression = str;
        if (valid) {
            type = str.trim().substring(0, 3);
        }
    }

    public boolean isValid() {
        return valid;
    }

    public boolean checkValid(String s) {
        String str = s.trim();
        if (!str.matches("(sin|cos)\\s*\\((.+)\\)(\\s*\\^\\s*[+-]?\\d+)?")) {
            return false;
        }

        Pattern p = Pattern.compile("(sin|cos)\\s*\\((.+)\\)" +
                "(\\s*\\^\\s*[+-]?\\d+)?");
        Matcher m = p.matcher(str);
        m.find();

        if (m.group(3) != null) {
            String temp = m.group(3).trim();
            exp = temp.substring(1).trim();
        } else {
            exp = "+1";
        }

        String temp = m.group(2).trim();
        if (temp.matches("[+-]?\\d+")) {
            factorOld = temp;
            factorDer = "0";
            return true;
        } else if (temp.matches("x(\\s*\\^\\s*[+-]?\\d+)?")) {
            if (temp.equals("x")) {
                factorOld = "x";
                factorDer = "1";
            } else {
                String tmp = temp.replaceAll("\\s*", "");
                BigInteger ttmp = new BigInteger(tmp.substring(2));
                factorOld = temp;
                factorDer = ttmp.toString() + "*x^" +
                        ttmp.subtract(BigInteger.ONE).toString();
            }
            return true;
        } else if (temp.matches("\\(.+\\)")) {
            temp = temp.substring(1, temp.length() - 1);
            AddSubComb a = new AddSubComb(temp);
            if (!a.isValid()) {
                return false;
            }
            factorOld = a.toString();
            factorDer = a.derivate();
            return true;
        } else if (temp.matches("(sin|cos)\\s*\\((.+)\\)" +
                "(\\s*\\^\\s*[+-]?\\d+)?")) {
            NestComb a = new NestComb(temp);
            if (!a.isValid()) {
                return false;
            }
            factorOld = a.toString();
            factorDer = a.derivate();
            return true;
        } else {
            return false;
        }
    }

    public String derivate() {
        TrigoFunc a = new TrigoFunc(type, factorOld, factorDer, exp);
        return a.derivate();
    }

    public String toString() {
        return "(" + expression + ")";
    }

}
