package utils.collector.bsbcms;

import cn.hutool.core.util.HexUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author lk
 * @date 2021/11/18
 * UDP工具
 */
@Slf4j
public class UdpClientUtil {
    private static DatagramSocket clientSocket = null;
    private static InetSocketAddress serverAddress = null;

    private static final String CHARSET_NAME = "utf-8";
    private static final String UDP_URL = "192.168.1.70";
    private static final Integer UDP_PORT = 7777;

    public static  DatagramSocket getDatagramSocket() throws SocketException{
        return (clientSocket == null) ? new DatagramSocket() : clientSocket;
    }

    public static InetSocketAddress getInetSocketAddress(String host, int port) throws SocketException{
        return (serverAddress == null) ? new InetSocketAddress(host, port) : serverAddress;
    }

    public static void send(String host, int port, String msg) throws IOException{
        try{
            log.info("UDP发送数据:" + msg);
            byte[] data = msg.getBytes(CHARSET_NAME);
            DatagramPacket packet = new DatagramPacket(data, data.length, getInetSocketAddress(host, port));
            getDatagramSocket().send(packet);
            log.info("发送完毕");
            getDatagramSocket().close();
         }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        String a = HexUtil.decodeHexStr("E5BC80E5A78BE6B58BE8AF95EFBC81EFBC81EFBC81EFBC81");
        System.out.println(a);
        for (int i = 0; i < 10; i++) {
            log.info(">>>>>>>>>>>第" + i + "次发送");
            UdpClientUtil.send(UdpClientUtil.UDP_URL, UdpClientUtil.UDP_PORT, "开始测试！！！！");
        }
    }
}
