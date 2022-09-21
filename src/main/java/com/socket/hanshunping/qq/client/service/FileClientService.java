package com.socket.hanshunping.qq.client.service;

import com.socket.hanshunping.StreamUtils;
import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * @description: 该类完成 文件传输服务
 * @Author guanqing
 * @Date 2022/9/21 8:26
 **/
public class FileClientService {
    
    /**
     * 发送文件
     * @Author guanqing
     * @Date 2022/9/21 8:28
     **/
    public void sendFileToOne(String src, String dest, String senderId, String getterId){
        /** 读取src文件 */
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        /** 需要将文件读取 */
        /*FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setBytes(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream!=null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            byte[] bytes = StreamUtils.streamToByteArray(bis);
            message.setBytes(bytes);

            ClientConnectServerThread clientConnectServerThread = ManageClientConnectServerThread.getClientConnectServerThread(senderId);
            Socket socket = clientConnectServerThread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis!=null){
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.io.tmpdir"));
    }
}
