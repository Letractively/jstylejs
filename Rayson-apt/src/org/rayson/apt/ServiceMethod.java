/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

class ServiceMethod extends RpcMethod {

	public ServiceMethod(ExecutableElement methodElement) {
		super(methodElement);
	}

	public void verifyProxyMethod(ProxyMethod proxyMethod,
			ProcessingEnvironment processingEnv) {
		// verify return type.
		if (!proxyMethod.getReturnType().toString()
				.equals(this.getReturnType().toString()))
			processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_METHOD_RETURN_TYPE_NOT_MATCH_SERVICE,
					this.getElement());

		// verify throw types.
		for (TypeMirror thrownType : proxyMethod.getThrownTypes()) {
			if (thrownType.toString().equals(Constants.RPC_EXCEPTION_NAME))
				continue;
			boolean foundMatchedException = false;
			for (TypeMirror serviceThrownType : this.getThrownTypes()) {
				if (serviceThrownType.toString().equals(thrownType.toString()))
					foundMatchedException = true;
			}
			if (!foundMatchedException)
				processingEnv.getMessager().printMessage(Kind.ERROR,
						Constants.PROXY_METHOD_EXCEPTION_NOT_MATCH,
						this.getElement());
		}
	}

	void verify(ProcessingEnvironment processingEnv) {
		// verify parameters.
		List<? extends VariableElement> parameters = this.getParameters();
		boolean parasPassed = true;
		if (parameters.isEmpty())
			parasPassed = false;
		for (int i = 0; i < parameters.size(); i++) {
			VariableElement parameter = parameters.get(i);
			if (i == 0) {
				if (!parameter.asType().toString()
						.equals(Constants.SESSION_INTERFACE_NAME))
					parasPassed = false;

			}
		}
		if (!parasPassed)
			processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.SERVICE_METHOD_FIRST_PARAMETER_MUST_BE_SESSION,
					this.getElement());

		// verify exception
		List<? extends TypeMirror> thrownTypes = this.getThrownTypes();
		boolean foundRpcException = false;
		for (TypeMirror typeMirror : thrownTypes) {
			if (typeMirror.toString().equals(Constants.RPC_EXCEPTION_NAME))
				foundRpcException = true;
		}
		if (foundRpcException)
			processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.SERVICE_METHOD_SHOULD_NOT_THROWN_RPCEXCEPTION,
					this.getElement());
	}
}
