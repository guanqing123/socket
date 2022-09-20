package com.socket.hanshunping.qq.common;

/**
 * 表示消息类型
 * @Author guanqing
 * @Date 2022/9/20 8:52
 **/
public interface MessageType {
    /** 登录验证成功 */
    String MESSAGE_LOGIN_SUCCEED = "1";
    /** 验证登录失败 */
    String MESSAGE_LOGIN_FAIL = "2";
    /** 消息类型为普通消息 */
    String MESSAGE_COMM_MES = "3";
    /** 请求得到在线用户列表 */
    String MESSAGE_GET_ONLINE_FRIEND = "4";
    /** 返回在线用户列表 */
    String MESSAGE_RET_ONLINE_FRIEND = "5";
    /** 客户端请求退出 */
    String MESSAGE_CLIENT_EXIT = "6";
    /** 群发消息 */
    String MESSAGE_TO_ALL_MES = "7";
    /** 发送文件 */
    String MESSAGE_FILE_MES = "8";
    /** 接收离线消息 */
    String MESSAGE_GET_NOT_ONLINE_MES = "9";
    /** 发送离线消息 */
    String MESSAGE_SET_NOT_ONLINE_MES = "10";
}
//可以扩展功能
