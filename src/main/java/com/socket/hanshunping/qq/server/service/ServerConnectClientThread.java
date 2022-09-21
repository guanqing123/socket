package com.socket.hanshunping.qq.server.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @description: 该类的一个对象和某个客户端保持通信
 * @Author guanqing
 * @Date 2022/9/20 11:36
 **/
public class ServerConnectClientThread extends Thread{

    private Socket socket;

    /** 连接到服务端的用户id */
    private String userId;
    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() { /** 这里线程出于run的状态,可以发送/接收消息 */

        while (true){
            try {
                System.out.println("服务端和客户端 "+userId+" 保持通信,读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                /** 后面会使用message, 根据message的类型,做相应的业务处理 */
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    /** 客户端要在线用户列表 */
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println(message.getSender() + " 要在线用户列表");
                    Message msg = new Message();
                    msg.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    msg.setContent(ManageServerConnectClientThread.getOnlineUser());
                    msg.setGetter(message.getSender());
                    oos.writeObject(msg);
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender() + " 退出");

                    /** 将这个客户端对应的线程,从集合中删除. */
                    ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                    socket.close(); /** 关闭连接 */
                    /** 退出线程 */
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    /** 根据message获取getterid，然后再得到对应的线程 */
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(message.getGetter());
                    if (serverConnectClientThread == null) { /** 说明目标对象离线 */
                        ArrayList<Message> messages = QQServer.getOffLineDb().get(message.getGetter());
                        if (messages == null) {
                            messages = new ArrayList<>();
                            QQServer.getOffLineDb().put(message.getGetter(), messages);
                        }
                        messages.add(message);
                    } else { /** 在线直接发 */
                        Socket socket = serverConnectClientThread.getSocket();
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        /** 转发, 提示如果客户不在线,可以保存到数据库,这样就可以实现离线留言 */
                        oos.writeObject(message);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    /** 需要遍历 管理线程的集合, 把所有的线程的socket得到(排除sender),然后把message进行转发即可 */
                    ManageServerConnectClientThread.getHm().forEach((k, serverConnectClientThread) -> {
                        /** 群发消息 */
                        if (!message.getSender().equals(k)) {
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                                oos.writeObject(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    ServerConnectClientThread serverConnectClientThread = ManageServerConnectClientThread.getServerConnectClientThread(message.getGetter());
                    if (serverConnectClientThread == null) { /** 说明目标对象离线 */
                        ArrayList<Message> messages = QQServer.getOffLineDb().get(message.getGetter());
                        if (messages == null) {
                            messages = new ArrayList<>();
                            QQServer.getOffLineDb().put(message.getGetter(), messages);
                        }
                        messages.add(message);
                    } else { /** 在线直接发 */
                        /** 根据getter id 获取到对应的线程, 将 message 对象转发 */
                        Socket socket = serverConnectClientThread.getSocket();
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message);
                    }
                } else {
                    System.out.println("其他类型的message, 暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }


}
