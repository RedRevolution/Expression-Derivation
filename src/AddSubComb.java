import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSubComb implements Derivate {
    private ArrayList<MultiComb> term = new ArrayList<>();
    private boolean valid;
    private String expr;
    private ArrayList<Integer> startIndex = new ArrayList<>();
    private ArrayList<Integer> endIndex = new ArrayList<>();
    private Stack<String> bracket = new Stack<>();
    private Stack<Integer> index = new Stack<>();
    private ArrayList<Integer> opStart = new ArrayList<>();

    public AddSubComb(String str) {
        valid = checkAndAdd(str);
        if (valid) {
            expr = str;
        }
    }

    public boolean isValid() {
        return valid;
    }

    private boolean checkAndAdd(String s) {
        String str = s.trim();
        if (str.charAt(0) != '+' && str.charAt(0) != '-') {
            str = "+ " + str;    //为表达式头加入+号,将表达式以(运算符+项)为单元分解
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
        decomseposeOp(str);
        for (int i = 1; i < opStart.size(); i++) {
            String temp = str.substring(opStart.get(i - 1), opStart.get(i));
            MultiComb tmp = new MultiComb(temp);
            if (tmp.isValid()) {
                this.term.add(tmp);
            } else {
                return false;
            }
        }
        String temp = str.substring(opStart.get(opStart.size() - 1));
        MultiComb tmp = new MultiComb(temp);
        if (tmp.isValid()) {
            this.term.add(tmp);
        } else {
            return false;
        }
        return true;
    }

    private void decomseposeOp(String s) {
        String str = s;
        Pattern p = Pattern.compile("([+-]\\s*)+");
        Matcher m = p.matcher(str);
        while (m.find()) {
            boolean flag = true;
            for (int i = 0; i < startIndex.size(); i++) {
                if (startIndex.get(i) < m.start()
                        && m.end() - 1 < endIndex.get(i)) {
                    flag = false;
                    break;
                }
            }
            if (m.start() == 0) {
                opStart.add(m.start());
                continue;
            }
            for (int i = m.start() - 1; i >= 0; i--) {
                if (str.charAt(i) == ' ' || str.charAt(i) == '\t') {
                    continue;
                }
                if (str.charAt(i) == '^' || str.charAt(i) == '*') {
                    flag = false;
                }
                break;
            }
            if (flag == false) {
                continue;
            }
            opStart.add(m.start());
        }
    }

    public String derivate() {
        String ans = new String();
        for (MultiComb i : term) {
            String tmp = i.derivate();
            ans = ans + tmp + "+";
        }
        if (ans.equals("")) {
            return "(0)";
        } else {
            return "(" + ans.substring(0, ans.length() - 1) + ")";
        }
    }

    public String toString() {
        return "(" + expr + ")";
    }
}
