package client;

import other.Macro;
import other.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Dialog implements Runnable {
    private JFrame frame;
    private JTextArea textArea1 = new JTextArea();  // 这个textArea用于显示聊天信息，不可编辑
    private String name;
    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter = null;

    public Dialog(String name, Socket socket) {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "无法连接服务器");
            return;
        }
        //System.out.println("Dialog is created");
        setLayout();
        this.name = name;
        this.show();
    }

    private void append(String s) {
        textArea1.append(s + "\r\n");
        // 这里是为了把光标放在所有文字末尾，起到发送消息强制定位到最后一行的作用
        textArea1.selectAll();
    }

    public void run() {
        // 不停读取服务器传来的Message，然后添加到textArea1中
        String s = "";
        while (true) {
            try {
                s = bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                close();
            }
            //System.out.println(s);
            if (s.contains(Macro.MESSAGE)) {
                append(Macro.decodeMessage(s).toString());
            }
        }
    }

    private void setLayout() {
        frame = new JFrame("对话");
        int height = 400;
        int width = 600;
        frame.setBounds(600, 300, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        textArea1.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        JTextArea textArea2 = new JTextArea();
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        textArea1.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
        textArea2.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
        JButton button = new JButton("发送");
        // 点击发送按钮时，向远程服务器发送输入信息（打包成Message后发送）
        button.addActionListener(e -> {
            String s = textArea2.getText();
            textArea2.setText("");
            printWriter.println(Macro.encodeMessage(new Message(name, s)));
            printWriter.flush();
        });

        int showHeight = height * 3 / 4;
        int buttonLength = height - (showHeight + 2);
        scrollPane1.setBounds(0, 0, width, showHeight);
        scrollPane2.setBounds(0, showHeight + 2, width - buttonLength - 2, buttonLength);
        button.setBounds(width - buttonLength, height - buttonLength,
                buttonLength * 2 / 3, buttonLength / 3);

        frame.getContentPane().add(scrollPane1);
        frame.getContentPane().add(scrollPane2);
        frame.getContentPane().add(button);

        // 窗体关闭时关闭socket等等(release)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                release();
            }
        });

        // 键盘监听，按了ENTER后，向远程服务器发送输入信息（打包成Message后发送）
        textArea2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int k = e.getKeyChar();
                if (k == KeyEvent.VK_ENTER) {
                    String s = textArea2.getText();
                    textArea2.setText("");
                    printWriter.println(Macro.encodeMessage(new Message(name, s)));
                    printWriter.flush();
                }
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
        release();
        frame.dispose();
    }

    private void release() {
        // 释放资源时同时通知远程服务器，注销当前用户
        if (socket != null && printWriter != null && bufferedReader != null) {
            printWriter.println(Macro.encodeLogout(name));
            printWriter.flush();
        }
        try {
            if (socket != null) socket.close();
            if (printWriter != null) printWriter.close();
            if (bufferedReader != null) bufferedReader.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
