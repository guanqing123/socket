package com.socket.hanshunping;

import java.io.*;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2022/9/19 9:10
 **/
public class StreamUtils {

    /**
     * 功能：将输入流转换成byte[],即可以把文件的内容输入到byte[]
     * @Author guanqing
     * @Date 2022/9/19 9:16
     **/
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//创建输出流对象
        byte[] b = new byte[1024];//字节数组
        int len;
        while ((len=is.read(b))!=-1){//循环读取
            bos.write(b, 0, len);//把读取到的数据,写入bos
        }
        byte[] array = bos.toByteArray();//然后将bos 转成字节数组
        bos.close();
        return array;
    }

    /**
     * 功能：将InputStream转换成String
     * @Author guanqing
     * @Date 2022/9/19 10:35
     **/
    public static String streamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line=reader.readLine())!=null){
            builder.append(line+"\r\n");
        }
        return builder.toString();
    }
}
