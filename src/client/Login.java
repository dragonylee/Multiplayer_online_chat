package client;

import other.Macro;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Login {
    private JFrame frame;
    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter = null;

    public Login() {
        try {
            socket = new Socket(Macro.serverIp, Macro.port);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "无法连接服务器");
            return;
        }
        setLayout();
        this.show();
    }

    private void setLayout() {
        frame = new JFrame("Login");
        frame.setBounds(600, 300, 350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 200);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(100, 100, 80, 25);
        panel.add(loginButton);
        // 点击登录按钮时，与服务器通信，发送登录信息，并且根据服务器的反馈进行处理（销毁并新建Dialog，或者提示登录失败）
        loginButton.addActionListener(e -> {
            loginButton.setEnabled(false);
            String name = userText.getText();
            String password = String.copyValueOf(passwordText.getPassword());
            String s = Macro.encodeLogin(name, password);
            printWriter.println(s);
            printWriter.flush();
            //System.out.println("you have sent " + s);
            try {
                s = bufferedReader.readLine();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            if (!s.equals(Macro.LOGIN_SUCCEED)) {
                JOptionPane.showMessageDialog(null, "登录失败，用户名不存在或密码错误");
            } else {
                JOptionPane.showMessageDialog(null, "登录成功！");
                new Thread(new Dialog(name, socket)).start();
                //System.out.println("I will close");
                close();
            }
            loginButton.setEnabled(true);
        });

        // 窗体关闭时关闭socket等等(release)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                release();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void close() {
        //release();
        frame.dispose();
    }

    private void release() {
        try {
            if (socket != null) socket.close();
            if (printWriter != null) printWriter.close();
            if (bufferedReader != null) bufferedReader.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
