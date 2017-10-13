package com.xuanwu.msggate.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 
 * <p><pre>
 * Description:Internet tools
 * </pre></p>
 * @author <a href="leasonliang@gmail.com">Leason Liang</a>
 * @Date 2011-03-21
 * @Version 1.0.0
 */
public class InetUtil {

	/**
	 * @author Leason Liang
	 * @return retrieve the local ip address(IPv4/IPv6)
	 */
	public static String getLocalIP() {
		InetAddress addr = null;
		Enumeration netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				System.out.println(ni.getName());
				addr = (InetAddress) ni.getInetAddresses().nextElement();
				if (!addr.isSiteLocalAddress() && !addr.isLoopbackAddress()
						&& addr.getHostAddress().indexOf(":") == -1) {
					System.out.println("本机的ip=" + addr.getHostAddress());
					break;
				} else {
					addr = null;
				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}

		return ipAddrStr;
	}
}
