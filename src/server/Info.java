package server;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Info {
    private HashMap<String, String> userNamePassword = new HashMap<>();

    public Info() {
        Scanner fin = null;
        try {
            fin = new Scanner(new File("user_info.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert fin != null;
        while (fin.hasNextLine()) {
            String s1 = fin.nextLine();
            String s2 = fin.nextLine();
            userNamePassword.put(s1, s2);
        }
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        return userNamePassword.containsKey(username) &&
                userNamePassword.get(username).equals(password);
    }
}
