/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

public interface ServiceRegistration extends Portable {

	public String getDescription();

	public String getName();

	public String[] getProxys();
}
