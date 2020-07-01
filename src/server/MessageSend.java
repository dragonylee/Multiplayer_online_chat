package server;

import other.Macro;
import other.Message;

import java.io.PrintWriter;
import java.util.HashMap;

public class MessageSend {
    private HashMap<String/*username*/, PrintWriter> users = new HashMap<>();

    public MessageSend() {

    }

    public synchronized void add(String username, PrintWriter printWriter) {
        users.put(username, printWriter);
    }

    public synchronized void remove(String username) {
        users.remove(username);
    }

    public synchronized boolean isOnline(String username) {
        return users.containsKey(username);
    }

    public synchronized boolean sendTo(String toUsername, Message message) {
        // 接受者或者发送者有不在线的，都发送失败
        if (!users.containsKey(toUsername)) {
            return false;
        } else if (!users.containsKey(message.getFrom())) {
            return false;
        }
        try {
            users.get(toUsername).println(Macro.encodeMessage(message));
            users.get(toUsername).flush();
            //System.out.println("sent to " + toUsername + "\t" + message);
        } catch (Exception e) {
            // 如果发送失败，说明与toUsername的连接已经断开，将它踢出在线列表
            users.remove(toUsername);
            e.printStackTrace();
        }
        return true;
    }

    // 这里加锁之后又调用加锁的函数，属于可重入锁（应该没问题吧……）
    public synchronized void sentToAll(Message message) {
        //System.out.println("user number: " + users.size());
        for (String username : users.keySet()) {
            boolean isSent = sendTo(username, message);
            if (isSent) {
                System.out.println(message.getFrom() + " successfully send to " + username);
            }
        }
    }
}
