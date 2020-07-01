package server;

import other.Macro;
import other.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerThread implements Runnable {
    private Info info;
    private MessageSend messageSend;
    private Socket socket;

    public ServerThread(Info info, MessageSend messageSend, Socket socket) {
        this.info = info;
        this.messageSend = messageSend;
        this.socket = socket;
    }

    public void run() {
        System.out.println("You have run a new ServerThread");
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String s = "";
        while (true) {
            try {
                s = bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (s == null) {
                break;
            }
            //System.out.println(s);
            if (s.contains(Macro.LOGIN)) {  // 登录请求
                ArrayList<String> t = Macro.decodeLogin(s);
                String username = t.get(0);
                String password = t.get(1);
                if (info.checkUsernameAndPassword(username, password)) {
                    printWriter.println(Macro.LOGIN_SUCCEED);
                    printWriter.flush();
                    messageSend.add(username, printWriter);
                } else {
                    printWriter.println(Macro.LOGIN_FAILED);
                    printWriter.flush();
                }
            } else if (s.contains(Macro.LOGOUT)) {  // 登出请求
                String username = Macro.decodeLogout(s);
                messageSend.remove(username);
            } else if (s.contains(Macro.MESSAGE)) {     // 发送消息请求（这里默认为广播，也就是全部发送）
                Message message = Macro.decodeMessage(s);
                messageSend.sentToAll(message);
            }
        }
        System.out.println("a serverThread is terminated");
    }
}

public class Server {
    public Server() throws IOException {
        Info info = new Info();
        MessageSend messageSend = new MessageSend();
        ServerSocket serverSocket = new ServerSocket(Macro.port);
        System.out.println("服务器已启动");
        while (true) {
            Socket socket = serverSocket.accept();
            String ip = socket.getInetAddress().getHostAddress();
            System.out.println("连接了一个ip： " + ip);
            new Thread(new ServerThread(info, messageSend, socket)).start();
        }
    }
}
