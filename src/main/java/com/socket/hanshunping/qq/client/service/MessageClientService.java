package com.socket.hanshunping.qq.client.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @description: 该类提供和消息相关的服务方法
 * @Author guanqing
 * @Date 2022/9/20 18:31
 **/
public class MessageClientService {

    /**
     * 发送消息给某人
     * @Author guanqing
     * @Date 2022/9/20 18:33
     **/
    public void sendMessageToOne(String content, String senderId, String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());

        try {
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(senderId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发消息给所有人
     * @Author guanqing
     * @Date 2022/9/20 21:03
     **/
    public void sendMessageToAll(String contents, String userId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(userId);
        message.setContent(contents);
        message.setSendTime(new Date().toString());

        try {
            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(userId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
