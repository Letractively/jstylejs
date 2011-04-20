/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;

public interface Staff {

	public Card getCard(String key);

	public int getId();

	public String getIp();

	public int getPort();

	public void putCard(String key, Portable value);
}