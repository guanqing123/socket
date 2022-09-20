package com.socket.hanshunping.demo2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @description: 使用字符流
 * @Author guanqing
 * @Date 2022/9/16 16:46
 **/
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("hello server");
        /** 插入一个换行符,表示写入的内容结束, 注意：要求对方使用readLine()!!! */
        writer.newLine();
        /** 如果使用的字符流,需要手动刷新,否则数据不会写入数据通道 */
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = reader.readLine();
        /** 输出 */
        System.out.println(s);

        reader.close();
        writer.close();
        socket.close();
    }
}
