package org.rayson.util;

import java.util.ArrayList;
import java.util.List;

import org.rayson.api.RpcService;

public final class ServiceParser {

	public static Class<? extends RpcService>[] getProtocols(RpcService instance) {
		List<Class<? extends RpcService>> list = new ArrayList<Class<? extends RpcService>>();
		Class serviceClass = instance.getClass();
		for (Class interfake : serviceClass.getInterfaces()) {
			if (RpcService.class.isAssignableFrom(interfake))
				list.add(interfake);
		}
		return list.toArray(new Class[0]);
	}
}
