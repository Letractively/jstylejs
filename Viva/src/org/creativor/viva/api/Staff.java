package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;

public interface Staff {

	public Card getCard(String key);

	public int getId();

	public String getIp();

	public short getPort();

	public void putCard(String key, Portable value);
}