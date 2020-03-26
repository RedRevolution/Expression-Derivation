import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {  //入口单独成类
        Scanner keyboard = new Scanner(System.in);
        if (!keyboard.hasNextLine()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        String expression = keyboard.nextLine();
        String str = expression;
        Pattern p = Pattern.compile("[^0123456789^+\\-sincox()*\\t\\ ]+");
        Matcher m = p.matcher(str);
        if (m.find() || str.matches("\\s*")) {    //非法字符和空字符串
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        p = Pattern.compile("\\^\\s*([+-]?\\d+)");
        m = p.matcher(str);
        while (m.find()) {
            BigInteger tmp = new BigInteger(m.group(1));
            if (tmp.compareTo(BigInteger.ZERO) <= 0
                    || tmp.compareTo(BigInteger.valueOf(10000)) > 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }
        AddSubComb a = new AddSubComb(expression);
        if (!a.isValid()) {
            a = null;  //回收对象
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        } else {
            System.out.println(a.derivate());
            System.exit(0);
        }
    }
}
