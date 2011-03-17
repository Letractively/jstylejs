package org.rayson.apt;

import javax.lang.model.element.ExecutableElement;

public class ServiceMethod extends RpcMethod {

	public ServiceMethod(ExecutableElement methodElement) {
		super(methodElement);
	}
}
