package com.socket.hanshunping.demo1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @description: 使用字节流
 * @Author guanqing
 * @Date 2022/9/16 14:14
 **/
public class Client {

    public static void main(String[] args) throws IOException {
        /**
         *  1、连接服务器（ip, 端口）
         *  解读：连接本机的 9999端口，如果连接成功，返回Socket对象
         */
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);

        /** 2、得到和 com.socket 对象关联的输出流对象 */
        OutputStream outputStream = socket.getOutputStream();

        /** 3、通过输出流，写入数据到 数据通道 */
        outputStream.write("hello, server".getBytes());
        /** 设置写入结束标记 */
        socket.shutdownOutput();

        /** 4、获取和socket关联的输入流，读取数据（字节），并显示 */
        InputStream inputStream = socket.getInputStream();
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = inputStream.read(buf))!=-1) {
            System.out.println(new String(buf, 0, readLen)); /** 根据读取到的实际长度, 显示内容 */
        }

        /** 5、关闭流对象和socket,必须关闭 */
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
