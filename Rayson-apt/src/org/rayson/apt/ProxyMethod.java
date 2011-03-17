package org.rayson.apt;

import javax.lang.model.element.ExecutableElement;

class ProxyMethod extends RpcMethod {

	public ProxyMethod(ExecutableElement methodElement) {
		super(methodElement);
	}

}
