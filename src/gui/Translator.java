package gui;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    public static String in(String message) {

        if (message.toLowerCase().equals("start")) {
            return "start";
        }
        if (message.toLowerCase().equals("ok")) {
            return "ok";
        }
        if (message.toLowerCase().equals("stop")) {
            return "stop";
        }
        Pattern pp = Pattern.compile("^\\d+$");
        Matcher mm = pp.matcher(message);
        if (mm.find()) {
            return message;
        }

        String[] strings = message.split("x|_"); // 0x0_1x1

        return "" + '{' + strings[0] + ';' + strings[1] + "},{" +
                strings[2] + ';' + strings[3] + '}';
    }

    public static String out(String message) {

        if (message.toLowerCase().equals("start")) {
            return "start";
        }
        if (message.toLowerCase().equals("ok")) {
            return "ok";
        }
        if (message.toLowerCase().equals("stop")) {
            return "stop";
        }
        Pattern pp = Pattern.compile("^\\d+$");
        Matcher mm = pp.matcher(message);
        if (mm.find()) {
            return message;
        }


        LinkedList<Integer> numbers = new LinkedList<>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }

        int x1 = numbers.get(0);
        int y1 = numbers.get(1);
        int x2 = numbers.get(2);
        int y2 = numbers.get(3);

        return "" + numbers.get(0) + 'x' + numbers.get(1) + '_' +
                numbers.get(2) + 'x' + numbers.get(3);
    }
}
