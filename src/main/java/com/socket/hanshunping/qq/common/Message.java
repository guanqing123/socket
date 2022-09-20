package com.socket.hanshunping.qq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 表示客户端和服务端通信时的消息对象
 * @Author guanqing
 * @Date 2022/9/20 8:47
 **/
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发送者 */
    private String sender;
    /** 接收者 */
    private String getter;
    /** 消息内容 */
    private String content;
    /** 发送时间 */
    private String sendTime;
    /** 消息类型[可以在接口定义消息类型] */
    private String mesType;

    /** 进行扩展 和文件相关成员 */
    private byte[] bytes;
    private int fileLen = 0;
    private String dest; /** 将文件传输到哪里 */
    private String src; /** 源文件路径 */

    public Message() {
    }

    public Message(String sender, String getter, String content, String sendTime, String mesType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.mesType = mesType;
    }
}
