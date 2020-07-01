package other;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String time;
    private String from;
    private String content;

    public Message(String from, String content) {
        this.from = from;
        this.content = content;
        this.time = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(
                new Date(System.currentTimeMillis())
        );
    }

    public Message(String time, String from, String content) {
        this.time = time;
        this.from = from;
        this.content = content;
    }

    public String getFrom() {
        return this.from;
    }

    public String getContent() {
        return this.content;
    }

    public String getTime() {
        return this.time;
    }

    public String toString() {
        return time + " " + from + ":\r\n" + content + "\r\n";
    }
}
