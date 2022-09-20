package com.socket.hanshunping.demo4;

import cn.stylefeng.guns.modular.mes.socket.hanshunping.StreamUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 文件下载
 * @Author guanqing
 * @Date 2022/9/19 13:17
 **/
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();

        String downloadFileName = "";
        InputStream in = socket.getInputStream();
        byte[] buf = new byte[1024];
        int readLen = 0;
        while ((readLen = in.read(buf))!=-1){
            downloadFileName += new String(buf, 0, readLen);
        }
        System.out.println("下载文件名："+ downloadFileName);

        String destFilePath = "C:\\Users\\information\\Desktop\\sea.jpg";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(destFilePath));
        byte[] bytes = StreamUtils.streamToByteArray(bis);

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        bos.write(bytes);
        socket.shutdownOutput();

        in.close();
        bos.close();
        bis.close();
        socket.close();
        serverSocket.close();
    }
}
