package com.socket.hanshunping.qq.server.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @description: 该类用于管理和客户端通信的线程
 * @Author guanqing
 * @Date 2022/9/20 13:10
 **/
public class ManageServerConnectClientThread {
    /** 我们把多个线程放入一个HashMap集合, key 就是用户id, value 就是线程 */
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    /** 将某个线程加入集合 */
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread){
        hm.put(userId, serverConnectClientThread);
    }

    /** 通过userId 可以得到对应的线程 */
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }

    /** 增加一个方法,从集合中,移除某个线程对象 */
    public static void removeServerConnectClientThread(String userId) {
        hm.remove(userId);
    }

    /** 这里编写方法, 可以返回在线用户列表 */
    public static String getOnlineUser(){
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()){
            onlineUserList += iterator.next() + " ";
        }
        return onlineUserList;
    }
}
