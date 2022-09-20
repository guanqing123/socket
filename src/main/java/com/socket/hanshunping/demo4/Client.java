package com.socket.hanshunping.demo4;

import cn.stylefeng.guns.modular.mes.socket.hanshunping.StreamUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @description: 文件下载
 * @Author guanqing
 * @Date 2022/9/19 13:52
 **/
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("天下无敌");
        writer.flush();
        socket.shutdownOutput();

        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        byte[] bytes = StreamUtils.streamToByteArray(bis);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("C:\\Users\\information\\Desktop\\download.jpg"));
        bos.write(bytes);

        bos.close();
        bis.close();
        writer.close();
        socket.close();
    }
}
