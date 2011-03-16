package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

class ServiceMethodVistor implements ElementVisitor<Void, AnnotationMirror> {

	private ProcessingEnvironment processingEnv;
	private AnnotationValue protocolClass;

	public ServiceMethodVistor(ProcessingEnvironment processingEnv,
			AnnotationValue protocolClass) {
		this.processingEnv = processingEnv;
		this.protocolClass = protocolClass;
	}

	@Override
	public Void visit(Element e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Element e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitExecutable(ExecutableElement e, AnnotationMirror p) {

		// verify parameters.
		List<? extends VariableElement> parameters = e.getParameters();
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
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_METHOD_FIRST_PARAMETER_MUST_BE_SESSION, e);

		// verify exception
		List<? extends TypeMirror> thrownTypes = e.getThrownTypes();
		boolean foundRpcException = false;
		for (TypeMirror typeMirror : thrownTypes) {
			if (typeMirror.toString().equals(Constants.RPC_EXCEPTION_NAME))
				foundRpcException = true;
		}
		if (!foundRpcException)
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_METHOD_MUST_THROWN_RPCEXCEPTION, e);
		return null;
	}

	@Override
	public Void visitPackage(PackageElement e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitType(TypeElement e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTypeParameter(TypeParameterElement e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitUnknown(Element e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVariable(VariableElement e, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

}
