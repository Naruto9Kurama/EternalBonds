package com.creator.common.utils;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {

    private static String TAG = "IPUtil";

    /**
     * 获取所有IP地址的
     *
     * @return 包含所有IP地址的列表。
     */
    public static List<InetAddress> getAllInetAddress() {
        List<InetAddress> internalIPAddresses = new ArrayList<>();

        try {
            // 获取所有网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 获取每个网络接口的InetAddress
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    internalIPAddresses.add(inetAddress);
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.d(TAG, String.valueOf(internalIPAddresses.size()));
        return internalIPAddresses;
    }


    /**
     * 获取所有指定网络IP地址的方法。
     *
     * @param isExternal 是否是外网，true：获取外网地址，false：获取内网地址
     * @return 包含所有指定网络IP地址的列表。
     */
    public static List<String> getIPAddresses(boolean isExternal) {

        List<String> internalIPAddresses = new ArrayList<>();
        //获取全部ip列表
        List<InetAddress> allInetAddress = getAllInetAddress();
        //遍历筛选ip
        allInetAddress.forEach(inetAddress -> {
            if (!inetAddress.isLoopbackAddress() && isInternalIPAddress(inetAddress) != isExternal) {
                // 添加符合条件的IP地址到列表中
                String ipAddress = inetAddress.getHostAddress();
                // 处理IPv6地址中的scope id
                int percentIndex = ipAddress.indexOf('%');
                if (percentIndex >= 0) {
                    ipAddress = ipAddress.substring(0, percentIndex);
                }
                internalIPAddresses.add(ipAddress);
            }
        });

        return internalIPAddresses;
    }


    /**
     * 判断是否为内部IP地址的方法。
     *
     * @param inetAddress 待判断的InetAddress。
     * @return 如果是内部IP地址则返回 true，否则返回 false。
     */
    private static boolean isInternalIPAddress(InetAddress inetAddress) {
        // 判断IPv6地址是否为本地链路地址或IPv4地址是否为站点本地地址
        if (isIPv6LinkLocal(inetAddress) || inetAddress.isSiteLocalAddress()) {
            return true;
        }

        // 判断IPv4地址是否在内网地址范围内
        String ipAddress = inetAddress.getHostAddress();
        if (ipAddress.startsWith("10.") || ipAddress.startsWith("192.168.") ||
                (ipAddress.startsWith("172.") && isBetween(ipAddress, 16, 31))) {
            return true;
        }

        return false;
    }

    /**
     * 判断IPv6地址是否为本地链路地址的方法。
     *
     * @param inetAddress 待判断的InetAddress。
     * @return 如果是IPv6本地链路地址则返回 true，否则返回 false。
     */
    private static boolean isIPv6LinkLocal(InetAddress inetAddress) {
        return inetAddress.isLinkLocalAddress() && inetAddress.getHostAddress().contains("%");
    }

    /**
     * 判断一个数值是否在指定的范围内的方法。
     *
     * @param ipAddressSegment IPv4地址的一部分。
     * @param start            范围的起始值。
     * @param end              范围的结束值。
     * @return 如果在范围内则返回 true，否则返回 false。
     */
    private static boolean isBetween(String ipAddressSegment, int start, int end) {
        int value = Integer.parseInt(ipAddressSegment.split("\\.")[1]);
        return value >= start && value <= end;
    }
}
