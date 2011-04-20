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

abstract class RpcMethod {

	private ExecutableElement element;
	private TypeMirror returnType;
	private List<? extends VariableElement> parameters;
	private String name;
	private List<? extends TypeMirror> thrownTypes;
	private int hashCode;

	public RpcMethod(ExecutableElement methodElement) {
		this.element = methodElement;
		this.returnType = methodElement.getReturnType();
		this.parameters = methodElement.getParameters();
		this.name = methodElement.getSimpleName().toString();
		this.thrownTypes = methodElement.getThrownTypes();
		this.hashCode = getHashCode();
	}

	public ExecutableElement getElement() {
		return element;
	}

	public List<? extends VariableElement> getParameters() {
		return parameters;
	}

	public String getName() {
		return name;
	}

	public TypeMirror getReturnType() {
		return returnType;
	}

	public List<? extends TypeMirror> getThrownTypes() {
		return thrownTypes;
	}

	private int getHashCode() {
		boolean wroteDot = false;
		StringBuffer sb = new StringBuffer();
		sb.append(this.name);
		sb.append("(");
		String parameterTypeName;
		for (VariableElement parameter : parameters) {
			parameterTypeName = parameter.asType().toString();
			// ignore session type.
			if (parameterTypeName.equals(Constants.SESSION_INTERFACE_NAME))
				continue;
			sb.append(parameterTypeName);
			sb.append(",");
			wroteDot = true;
		}
		if (wroteDot)
			sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString().hashCode();
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

}