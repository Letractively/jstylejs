package org.creativor.viva.conf;

import java.util.Collections;
import java.util.List;

public class Servants implements Configuration {
	private List<String> nodes;

	public List<String> getNodes() {
		return Collections.unmodifiableList(nodes);
	}
}
