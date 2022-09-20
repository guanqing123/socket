package com.socket.liaoxuefeng;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2022/8/28 10:33
 **/
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        System.out.println("server is running...");
        for (;;){
            Socket socket = ss.accept();
            System.out.println("connected from " + socket.getRemoteSocketAddress());
            Thread t = new Handler(socket);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream input = this.socket.getInputStream()) {
            try (OutputStream output = this.socket.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");
        writer.flush();
        for (;;){
            String s = reader.readLine();
            if (s.equals("bye")){
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: "+ s + "\n thread=" + Thread.currentThread().getName());
            writer.flush();
        }
    }
}
