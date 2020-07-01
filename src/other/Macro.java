package other;

import java.util.ArrayList;
import java.util.Arrays;

public class Macro {
    // server
    public static int port = 8888;
    public static String serverIp = "";

    // login
    public static String LOGIN = "login_&qw12#%#dsfe!~&%321]]efwe1236fdz,1=";
    public static String LOGIN_SPLIT = "wq3,<~!@qwex";

    public static String encodeLogin(String username, String password) {
        return LOGIN + username + LOGIN_SPLIT + password;
    }

    public static ArrayList<String> decodeLogin(String in) {
        if (in == null) {
            return new ArrayList<>(Arrays.asList("", ""));
        }
        String[] t = in.replaceFirst(LOGIN, "").split(LOGIN_SPLIT);
        if (t.length < 2) {
            return new ArrayList<>(Arrays.asList("", ""));
        }
        return new ArrayList<>(Arrays.asList(t[0], t[1]));
    }

    // login status
    public static String LOGIN_SUCCEED = "login succeed";
    public static String LOGIN_FAILED = "login failed";

    // log out
    public static String LOGOUT = "logout_fwe12&!~;;;,,2,sfew324dbeg";

    public static String encodeLogout(String username) {
        return LOGOUT + username;
    }

    public static String decodeLogout(String in) {
        return in.replaceFirst(LOGOUT, "");
    }

    // message
    public static String MESSAGE = "abi5,;:120cvbnewq``~_;";
    public static String MESSAGE_SPLIT = "%,,:;12@43aibde_32=12!!]jew";

    public static String encodeMessage(Message message) {
        return MESSAGE + message.getTime() + MESSAGE_SPLIT + message.getFrom()
                + MESSAGE_SPLIT + message.getContent();
    }

    public static Message decodeMessage(String s) {
        String[] t = s.replaceFirst(MESSAGE, "").split(MESSAGE_SPLIT);
        return new Message(t[0], t[1], t[2]);
    }
}
