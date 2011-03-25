package org.rayson.api;

public interface ServiceRegistration extends Portable {

	public String getDescription();

	public String getName();

	public String[] getProxys();
}
