/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva;

public final class HashCoder {
	public static int getHashCode(String key) {
		if (key == null)
			throw new IllegalArgumentException("Key should not be null");
		// TODO: Need a better hash code.
		return key.hashCode();
	}
}
