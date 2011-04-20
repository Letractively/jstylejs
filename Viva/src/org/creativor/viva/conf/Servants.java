/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.conf;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nick Zhang
 */
public class Servants implements Configuration {
	private List<String> nodes;

	public List<String> getNodes() {
		return Collections.unmodifiableList(nodes);
	}
}
