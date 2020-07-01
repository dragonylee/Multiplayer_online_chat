## Multiplayer_online_chat

<br/>

### Introduction

This is a multiplayer online chat room, which implements:

- GUI on user side

- multiplayer online chat
- login with username and password(not register)

Both the server and client code will run on a Java-enabled machine, where the client machine must supports the graphical interface. 

<br/>

### Usage

I replace my server's IP address in `src/other/Macro` by `""` for safety, so first you may change it to your server IP and set the available port:

```java
// server
public static int port = 8888;
public static String serverIp = "111.111.11.11";
```

Then you should check the class `src/Main` and decide whether to run server code or client code by annotation. for example, code below runs client code.

```java
public static void main(String[] args) throws IOException {
	//Server server = new Server();
	Client client = new Client();
}
```

I didn't separate the server code and the client code because they have a lot in common.

Finally you can pack separately a `server.jar` and a `client.jar`, then run them in expected machines. You need to make sure that there exist a file `user_info.txt` in the same path with `server.jar` , which records the username and password, for example, 

```
user1
123456
user2
123456
```

because the chat system doesn't support registration, `user_info.txt` gives pre-set account information.

<br>

### Finally

