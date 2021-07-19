package org.anlntse.utils;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtils {

    //IP地址转数字
    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            // left shifting 24, 16, 8, 0 and bitwise OR
            result |= ip << (i * 8);
        }
        return result;
    }

    //数字转IP地址
    public static String longToIp(long ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            ip = ip >> 8;
        }
        return sb.toString();
    }

    public static boolean isIp(String addr) {
        if (addr == null || addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.matches();
    }

    public static boolean pingReachable(String ip) {
        String[] ipSec = ip.split("\\.");
        try {
            //do not use InetAddress.getByName(), it would do reverse DNS lookup.
            InetAddress inet = InetAddress.getByAddress(new byte[]{
                    (byte) Integer.parseInt(ipSec[0]),
                    (byte) Integer.parseInt(ipSec[1]),
                    (byte) Integer.parseInt(ipSec[2]),
                    (byte) Integer.parseInt(ipSec[3])
            });
            return inet.isReachable(2000); //timeout 2 seconds
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String ip = "192.168.1.1";
        String ip2 = "172.16.119.224";
        long ipL = ipToLong(ip);
        long ipL2 = ipToLong(ip2);
        System.out.println(ip + " ==> " + ipL);
        System.out.println(ip2 + " ==> " + ipL2);

        String ipS = longToIp(ipL);
        String ipS2 = longToIp(ipL2);
        System.out.println(ipL + " ==> " + ipS);
        System.out.println(ipL2 + " ==> " + ipS2);

    }

}
