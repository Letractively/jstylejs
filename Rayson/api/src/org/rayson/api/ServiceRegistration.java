package org.rayson.api;

public interface ServiceRegistration extends Transportable {
	public String getDescription();

	public String getName();

	public String[] getProtocols();
}
