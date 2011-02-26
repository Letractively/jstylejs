package org.rayson.api;

public interface ServiceDescription extends Transportable {
	public String[] getProtocols();

	public String getName();
}
