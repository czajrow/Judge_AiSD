package tools;

import java.util.regex.Pattern;

public class MoveValidator {

    public static boolean isConfirmationMessageValid(String message) {
        return message.toLowerCase().equals("ok");
    }

    public static boolean isMoveValid(String move) {

        return Pattern.matches("^\\{\\d+;\\d+},\\{\\d+;\\d+}$", move);
    }

}
