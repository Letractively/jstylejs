package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.tools.Diagnostic.Kind;

class ServiceTypeVisitor extends AbstractElementVisitor6<Boolean, Void> {

	private Messager messager;
	private Class[] protocols;

	public ServiceTypeVisitor(Messager messager, Class[] protocols) {
		this.messager = messager;
		this.protocols = protocols;
	}

	@Override
	public Boolean visitPackage(PackageElement e, Void p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visitType(TypeElement e, Void p) {
		List<? extends Element> enclosedElements = e.getEnclosedElements();
		for (Element element : enclosedElements) {
			element.accept(this, null);
		}
		return true;
	}

	@Override
	public Boolean visitVariable(VariableElement e, Void p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visitExecutable(ExecutableElement e, Void p) {
		messager.printMessage(Kind.ERROR, "method error", e);
		return true;
	}

	@Override
	public Boolean visitTypeParameter(TypeParameterElement e, Void p) {
		// TODO Auto-generated method stub
		return true;
	}
}
