package com.socket.hanshunping.demo2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 使用字符流
 * @Author guanqing
 * @Date 2022/9/16 16:39
 **/
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);

        Socket socket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = reader.readLine();
        /** 输出 */
        System.out.println(s);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("hello client");
        /** 插入一个换行符,表示回复内容的结束 */
        writer.newLine();
        writer.flush();

        writer.close();
        reader.close();
        socket.close();
        serverSocket.close();
    }
}
