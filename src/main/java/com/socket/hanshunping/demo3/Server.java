package com.socket.hanshunping.demo3;

import com.socket.hanshunping.StreamUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 文件上传
 * @Author guanqing
 * @Date 2022/9/19 9:19
 **/
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8888);

        Socket socket = serverSocket.accept();

        /** 读取客户端发送的数据, 通过socket得到输入流 */
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        byte[] bytes = StreamUtils.streamToByteArray(bis);

        /** 将得到的bytes数组,写入到指定的路径,就得到一个文件了 */
        String property = System.getProperty("java.io.tmpdir");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(property + "\\sea2.jpg"));
        bos.write(bytes);
        bos.close();

        /** 向客户端回复 "收到图片" */
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("收到图片");
        writer.flush();
        socket.shutdownOutput();

        /** 关闭其他资源 */
        writer.close();
        bis.close();
        socket.close();
        serverSocket.close();
    }
}
