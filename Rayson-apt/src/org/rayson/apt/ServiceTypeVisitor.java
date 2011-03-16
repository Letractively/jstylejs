package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

class ServiceTypeVisitor extends
		AbstractElementVisitor6<Boolean, AnnotationMirror> {

	private ProcessingEnvironment processingEnv;
	private AnnotationValue proxyAnnotation;

	public ServiceTypeVisitor(ProcessingEnvironment processingEnv,
			AnnotationValue proxyAnnotation) {
		this.processingEnv = processingEnv;
		this.proxyAnnotation = proxyAnnotation;
	}

	@Override
	public Boolean visitPackage(PackageElement e, AnnotationMirror p) {
		return true;
	}

	@Override
	public Boolean visitType(TypeElement e, AnnotationMirror p) {

		// 1.find whether interface.
		if (!e.getKind().isInterface()) {
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_MUST_BE_INTERFACE, e);
			return false;
		}

		// 2.find whether implements RpcService.
		List<? extends TypeMirror> interfaces = e.getInterfaces();
		if (!findServiceInterface(interfaces)) {
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.Proxy_ANNOTATIONED_MUST_SERVICE, e);
			return false;
		}

		// 3.visit protocols annotation interfaces.
		if (!proxyAnnotation.accept(new ProtocolAnnotationValueVisitor(
				this.processingEnv, e), p)) {
			return false;
		}
		// 4.verify all methods.
		List<ExecutableElement> methods = ElementFilter.methodsIn(e
				.getEnclosedElements());
		for (ExecutableElement executableElement : methods) {
			this.visitExecutable(executableElement, p);
		}
		return true;
	}

	private boolean findServiceInterface(List<? extends TypeMirror> interfaces) {
		for (TypeMirror typeMirror : interfaces) {
			if (typeMirror.toString().equals(
					Constants.RPCSERVICE_INTERFACE_NAME))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visitVariable(VariableElement e, AnnotationMirror p) {
		return true;
	}

	@Override
	public Boolean visitExecutable(ExecutableElement e, AnnotationMirror p) {
		e.accept(new ServiceMethodVistor(this.processingEnv,
				this.proxyAnnotation), p);
		return true;
	}

	@Override
	public Boolean visitTypeParameter(TypeParameterElement e, AnnotationMirror p) {
		return true;
	}
}
