/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

/**
 *
 * @author Nick Zhang
 */
class ProxyMethod extends RpcMethod {

	public ProxyMethod(ExecutableElement methodElement) {
		super(methodElement);
	}

	public void verify(ServiceMethod serviceMethod,
			ProcessingEnvironment processingEnv) {
		// must throw rpcexception.
		boolean foundRpcException = false;
		for (TypeMirror thrownType : this.getThrownTypes()) {
			if (thrownType.toString().equals(Constants.RPC_EXCEPTION_NAME)) {
				foundRpcException = true;
				break;
			}
		}
		if (!foundRpcException)
			processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_METHOD_MUST_THROWN_RPCEXCEPTION,
					serviceMethod.getElement());

		// must has no session parameter.
		VariableElement sessionPara = null;
		for (VariableElement variableElement : this.getParameters()) {
			if (variableElement.asType().toString()
					.equals(Constants.SESSION_INTERFACE_NAME))
				sessionPara = variableElement;
		}
		if (sessionPara != null)
			processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_METHOD_PARA_SHOULD_NOT_BE_SESSION,
					serviceMethod.getElement());

	}

}
