/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;

public interface Card {

	public int getHashCode();

	public String getKey();

	public Portable getValue();
}
