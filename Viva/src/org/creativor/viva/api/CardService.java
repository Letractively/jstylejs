/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

@Proxy(CardProxy.class)
public interface CardService extends RpcService {

	public Portable get(Session session, int hashCode);

	public void put(Session session, int hashCode, Portable value);
}
