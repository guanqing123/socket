package com.socket.hanshunping.qq.server.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;
import com.socket.hanshunping.qq.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2022/9/21 10:59
 **/
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {

        /** 为了可以推送多次新闻,使用while */
        while (true){
            System.out.println("请输入服务器要推送的新闻/消息[输入exit表示退出推送服务]");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            /** 构建一个消息, 群发消息 */
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());

            /** 遍历当前所有的通信线程, 得到socket,并发送message */
            ManageServerConnectClientThread.getHm().forEach((k, serverConnectClientThread) -> {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
