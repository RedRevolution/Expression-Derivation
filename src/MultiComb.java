import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiComb implements Derivate {

    private boolean valid;
    private ConstantFunc constant;
    private TrigoFunc sin = new TrigoFunc("sin(x)^0");
    private TrigoFunc cos = new TrigoFunc("cos(x)^0");
    private PowerFunc power = new PowerFunc("x^0");
    private ArrayList<NestComb> nest = new ArrayList<>();
    private ArrayList<AddSubComb> poly = new ArrayList<>();
    private ArrayList<Integer> startIndex = new ArrayList<>();
    private ArrayList<Integer> endIndex = new ArrayList<>();
    private Stack<String> bracket = new Stack<>();
    private Stack<Integer> index = new Stack<>();
    private static String[] regex = new String[6];

    static {
        regex[0] = "[+-]?\\d+";   //常数因子
        regex[1] = "x(\\s*\\^\\s*[+-]?\\d+)?";   //幂函数因子
        regex[2] = "sin\\s*\\(\\s*x\\s*\\)(\\s*\\^\\s*[+-]?\\d+)?";  //sin函数因子
        regex[3] = "cos\\s*\\(\\s*x\\s*\\)(\\s*\\^\\s*[+-]?\\d+)?";  //cos函数因子
        regex[4] = "\\(.+\\)";  //表达式因子
        regex[5] = "(sin|cos)\\s*\\(.+\\)(\\s*\\^\\s*[+-]?\\d+)?";  //嵌套因子
    }

    public MultiComb(String str) {
        valid = checkAndAdd(str);
    }

    private boolean checkAndAdd(String s) {
        String str = s;
        int k = 0;
        Pattern p = Pattern.compile("[^+\\-\\ \\t]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            k = m.start();
        } else {
            return false;
        }
        char chr = str.charAt(k);
        char pchr = str.charAt(k - 1);
        String temp;
        if (chr >= '0' && chr <= '9' && (pchr == '+' || pchr == '-')) {
            temp = str.substring(0, k - 1);
            temp = temp.replaceAll("\\s*", "");
            if (!temp.equals("") && !temp.matches("[+-]{1,2}")) {
                return false;
            }
            str = str.substring(k - 1);
        } else {
            temp = str.substring(0, k);
            temp = temp.replaceAll("\\s*", "");
            if (!temp.matches("[+-]{1,2}")) {
                return false;
            }
            str = str.substring(k);
        }
        if (temp.equals("") || temp.equals("+")
                || temp.equals("++") || temp.equals("--")) {
            constant = new ConstantFunc("+1");
        } else {
            constant = new ConstantFunc("-1");
        }

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                bracket.push("(");
                index.push(i);
            }
            if (str.charAt(i) == ')') {
                if (bracket.empty() || bracket.peek().equals(")")) {
                    return false;
                } else if (bracket.peek().equals("(") && bracket.size() == 1) {
                    startIndex.add(index.peek());
                    endIndex.add(i);
                }
                bracket.pop();
                index.pop();
            }
        }
        if (!bracket.empty()) {
            return false;
        }

        if (!decompose(str)) {
            return false;
        }

        return true;
    }

    private boolean decompose(String s) {
        String str = s;
        String temp;
        Pattern p = Pattern.compile("\\*");
        Matcher m = p.matcher(str);
        int last = 0;
        while (m.find()) {
            boolean flag = true;
            for (int i = 0; i < startIndex.size(); i++) {
                if (startIndex.get(i) < m.start()
                        && m.end() - 1 < endIndex.get(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag == false) {
                continue;
            }

            temp = str.substring(last, m.start());
            temp = temp.trim();
            if (!addFactor(temp)) {
                return false;
            }
            last = m.end();
        }

        temp = str.substring(last);
        temp = temp.trim();
        if (temp.equals("") || !addFactor(temp)) {
            return false;
        }

        return true;
    }

    public boolean isValid() {
        return valid;
    }

    private boolean addFactor(String temp) {
        if (temp.matches(regex[0])) {
            constant.mul(temp);
        } else if (temp.matches(regex[1])) {
            power.mul(temp);
        } else if (temp.matches(regex[2])) {
            sin.mul(temp);
        } else if (temp.matches(regex[3])) {
            cos.mul(temp);
        } else if (temp.matches(regex[4])) {
            String tmp = temp.substring(1, temp.length() - 1);
            AddSubComb a = new AddSubComb(tmp);
            if (!a.isValid()) {
                return false;
            }
            poly.add(a);
        } else if (temp.matches(regex[5])) {
            NestComb a = new NestComb(temp);
            if (!a.isValid()) {
                return false;
            }
            nest.add(a);
        } else {
            return false;
        }
        return true;
    }

    private String get(String str, int index) {
        /*
         constant 0
         power    1
         sin      2
         cos      3
         poly     4 ~ 4+poly.size()-1
         nest     4+poly.size() ~ 4+poly.size()+nest.size()-1
        */
        if (index == 0) {
            if (str.equals("old")) {
                return constant.toString();
            } else {
                return constant.derivate();
            }
        } else if (index == 1) {
            if (str.equals("old")) {
                return power.toString();
            } else {
                return power.derivate();
            }
        } else if (index == 2) {
            if (str.equals("old")) {
                return sin.toString();
            } else {
                return sin.derivate();
            }
        } else if (index == 3) {
            if (str.equals("old")) {
                return cos.toString();
            } else {
                return cos.derivate();
            }
        } else if (3 < index && index < 4 + poly.size()) {
            if (str.equals("old")) {
                return poly.get(index - 4).toString();
            } else {
                return poly.get(index - 4).derivate();
            }
        } else {
            if (str.equals("old")) {
                return nest.get(index - poly.size() - 4).toString();
            } else {
                return nest.get(index - poly.size() - 4).derivate();
            }
        }
    }

    public String derivate() {
        String ans = new String();
        for (int i = 1; i < 4 + poly.size() + nest.size(); i++) {

            for (int j = 0; j < i; j++) {
                ans = ans + get("old", j) + "*";
            }

            ans = ans + get("der", i) + "*";

            for (int j = i + 1; j < 4 + poly.size() + nest.size(); j++) {
                ans = ans + get("old", j) + "*";
            }
            ans = ans.substring(0, ans.length() - 1) + "+";
        }
        return "(" + ans.substring(0, ans.length() - 1) + ")";
    }

}
