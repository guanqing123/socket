package com.socket.hanshunping.qq.server.service;

import com.socket.hanshunping.qq.common.Message;
import com.socket.hanshunping.qq.common.MessageType;
import com.socket.hanshunping.qq.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器,在监听9999,等待客户端的连接,并保持通信
 * @Author guanqing
 * @Date 2022/9/20 11:22
 **/
public class QQServer {

    private ServerSocket ss;

    /** 创建一个集合,存放多个用户,如果是这些用户登录,就认为是合法 */
    private static ConcurrentHashMap<String , User> validUser = new ConcurrentHashMap<>();

    /** 离线消息 */
    private static ConcurrentHashMap<String , ArrayList<Message>> offLineDb = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<Message>> getOffLineDb() {
        return offLineDb;
    }

    static {
        validUser.put("100",new User("100","123456"));
        validUser.put("200",new User("200","123456"));
        validUser.put("300",new User("300","123456"));
        validUser.put("至尊宝",new User("至尊宝","123456"));
        validUser.put("菩提老祖",new User("紫霞仙子","123456"));
        validUser.put("紫霞仙子",new User("菩提老祖","123456"));
    }

    /** 验证用户是否有效 */
    private boolean checkUser(String userId, String passwd){
        User user = validUser.get(userId);
        if (user == null){
            return false;
        }
        if (!user.getPasswd().equals(passwd)){
            return false;
        }
        return true;
    }

    public QQServer(){
        try  {
            System.out.println("服务端在9999端口进行监听");
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true){ /** 当和某个客户端连接后,会继续监听,因此while */
                Socket socket = ss.accept(); /** 如果没有客户端连接,就会阻塞在这里 */
                /** 得到socket关联的对象输入流 */
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject(); /** 读取客户端发送的User对象 */

                /** 得到socket关联的对象输出流 */
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                /** 创建一个Message对象,准备回复客户端 */
                Message message = new Message();
                if (checkUser(user.getUserId(), user.getPasswd())){ /** 登录通过 */
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    /** 将message对象回复客户端 */
                    oos.writeObject(message);

                    /** 检查离线文件 */
                    ArrayList<Message> messages = offLineDb.get(user.getUserId());
                    if (messages!=null && messages.size()>0) {
                        for (Message msg : messages) {
                            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                            os.writeObject(msg);
                        }
                        offLineDb.remove(user.getUserId());
                    }

                    /** 创建一个线程,和客户端保持通信,该线程需要持有socket对象 */
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();
                    /** 把该线程对象,放入到一个结合中,进行管理 */
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                } else { /** 登录失败 */
                    System.out.println("用户 id=" + user.getUserId() + " pwd=" + user.getPasswd() + " 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    /** 关闭socket */
                    socket.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                /** 如果服务器退出了while,说明服务器端不在监听,因此关闭ServerSocket */
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
