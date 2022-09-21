package com.socket.hanshunping.qq.client.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
    /** 该线程需要持有Socket */
    private Socket socket;
    /** 构造器可以接受一个socket对象 */
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        /** Thread需要在后台与服务器进行通信，持续通信所以while */
        while (true) {
            try {
                System.out.println("客户端线程，等待读取服务器端发送的消息。");
                /** 得到输入流 */
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                /** 处理读入的对象 */
                Object o = ois.readObject();/** 如果服务器没有发送objec对象,线程会阻塞在这里 */
                Message message = (Message) o;
                /** 对得到的信息进行处理 */
                /** 判断这个message类型,然后做相应的业务处理 */
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    /** 取出在线列表,并显示 */
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n=======当前在线用户列表=======");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户: " + onlineUsers[i]);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    /** 把从服务器转发的消息, 显示到控制台即可  */
                    System.out.println("\n" + message.getSender() + " 对我( " + message.getGetter() + " )说: " + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    /** 显示在客户端的控制台 */
                    System.out.println("\n" + message.getSender() + " 对大家说：" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    /** 拓展: 这里如果让用户指定保存的路径,如何实现? */
                    /** 转存文件 */
                    System.out.println("\n" + message.getSender() + " 发送文件给" + message.getGetter());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(message.getDest()));
                    bos.write(message.getBytes());
                    bos.close();
                    System.out.println("\n" + "文件接收ok." + "文件地址：" + message.getDest());
                } else {
                    System.out.println("是其他类型的message,暂时不处理....");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /** 为了更方便的得到socket */
    public Socket getSocket() {
        return socket;
    }
}
