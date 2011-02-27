package org.rayson.api;

public interface ServiceRegistration extends Transportable {
	public String[] getProtocols();

	public String getName();

	public String getDescription();
}
