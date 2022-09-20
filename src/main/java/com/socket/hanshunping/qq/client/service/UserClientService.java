package com.socket.hanshunping.qq.client.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;
import com.socket.hanshunping.qq.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 该类完成用户登录验证和用户注册等功能
 * @Author guanqing
 * @Date 2022/9/20 9:14
 **/
public class UserClientService {

    /** 因为我们可能在其他地方使用user信息,因此作为成员属性 */
    private User user = new User();
    /** 因为Socket在其他地方也可能使用,因此作为属性 */
    private Socket socket;

    /** 根据userId 和 pwd 到服务器验证该用户是否合法 */
    public boolean checkUser(String userId, String pwd){
        boolean b = false;

        /** 创建User对象 */
        user.setUserId(userId);
        user.setPasswd(pwd);

        try {
            /** 与服务器建立链接 */
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            /** 得到ObjectOutputStream对象流,并将用户信息写入，发送出去后，服务端返回返回值 */
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            /** 读取从服务器返回的Message对象 */
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            /** 根据返回来的服务器的信息来判断有没有登录成功，密码账户数据的对比是在服务端进行的 */
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){/** 登录ok */
                /** 创建一个和服务器保持通信的线程 让线程持有Socket 所以创建ClientConnectServerThread */
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                /** 启动一个客户端线程  执行run方法 */
                clientConnectServerThread.start();
                /** 这里为了后面客户端的扩展,我们将线程放入到集合管理 */
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                b = true;
            }else {
                /** 登录失败 */
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /** 向服务器端请求在线用户列表 */
    public void onlineFriendList(){

        /** 发送一个Message, 类型MESSAGE_GET_ONLINE_FRIEND */
        Message message = new Message();
        message.setSender(user.getUserId());
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);

        /** 发送给服务器 */
        try {
            /** 从管理线程的集合中,通过userId,得到这个线程对象 */
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId());
            /** 通过这个线程得到关联的socket */
            Socket socket = clientConnectServerThread.getSocket();
            /** 得到当前线程的Socket 对应的 ObjectOutputStream对象 */
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            /** 发送一个Message对象,向服务端要求在线用户列表 */
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 编写方法,退出客户端,并给服务端发送一个退出系统的message对象 */
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        /** 一定要指定我是哪个客户端 */
        message.setSender(user.getUserId());

        /** 发送给服务器 */
        try {
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId());
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + " 退出系统 ");
            System.exit(0); /** 结束进程 */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
