package com.example.demo.utils;

import org.apache.kafka.common.protocol.types.Field;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;

public class IpUtils {

    /**
     *
     *
     * @param str
     * @return 对应地址值的字符串，0如果传入的是空
     */
    public static int parseIntIpv4(String str){
        if (str.equals("")||str.equals("* * *")){
            return 0;
        }
        String[] ipSlices = str.split("\\.");

        int result = 0;

        for (int i = 0; i < ipSlices.length; i++) {
            int intSlice = Integer.parseInt(ipSlices[i]) << 8 * i;
            result = result | intSlice;
        }
        return result;
    }

    public static byte[] parseIpv6(String str) throws UnknownHostException {
        if (str.equals("")||str.equals("* * *")){
            byte[] r=new byte[16];
            for (int i = 0; i < 16; i++) {
                r[i]=0;
            }
            return r;
        }
        return  InetAddress.getByName(str).getAddress();
    }

    public static String ipv6ToBase64String(byte[] ipv6){
        if(ipv6.length!=16){
            throw new RuntimeException("invalid ipv6 byte array with length="+ipv6.length);
        }
        return Base64.getEncoder().encodeToString(ipv6);
    }
    public static String ipv6ToString(byte[] ipv6) throws UnknownHostException {
        if(ipv6==null){
            return "* * * ";
        }
        if(ipv6.length!=16){
            throw new RuntimeException("invalid ipv6 byte array with length="+ipv6.length);
        }
        boolean flag=true;
        String r= InetAddress.getByAddress(ipv6).getHostAddress();
        return r.equals("0:0:0:0:0:0:0:0")?"* * * ":r;
    }
    public static String ipv4ToString(int ipv4){
        if (ipv4==0){
            return "* * *";
        }
        int[] ip =new int[4];
        for (int i = 0; i <4; i++) {
            ip[i] = ipv4 & 0xff;
            ipv4 = ipv4 >> 8;
        }

        return ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];
    }

}
