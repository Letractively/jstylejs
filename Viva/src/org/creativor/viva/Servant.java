/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Nick Zhang
 */
public class Servant {

	private static String SERVANT_ADDRESS_HINTS = "Servant address format should be \"ip:port\"";

	public static Servant fromAddress(String servantAddress)
			throws IllegalArgumentException {
		if (servantAddress == null)
			throw new IllegalArgumentException();
		List<String> strList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(servantAddress, ":");
		while (tokenizer.hasMoreElements()) {
			String string = (String) tokenizer.nextElement();
			strList.add(string);
		}
		if (strList.size() != 2)
			throw new IllegalArgumentException(SERVANT_ADDRESS_HINTS);
		String ip = strList.get(0);
		short portNumber;
		try {
			portNumber = Short.parseShort(strList.get(1));
		} catch (Exception e) {
			throw new IllegalArgumentException(SERVANT_ADDRESS_HINTS);
		}

		Servant servant = new Servant(portNumber, ip);
		return servant;
	}

	private InetSocketAddress address;

	private Servant(short portNumber, String ip) {
		this.address = new InetSocketAddress(ip, portNumber);
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return this.address.toString();
	}
}