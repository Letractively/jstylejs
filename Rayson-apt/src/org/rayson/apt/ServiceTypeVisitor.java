package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.tools.Diagnostic.Kind;

class ServiceTypeVisitor extends AbstractElementVisitor6<Boolean, Void> {

	private ProcessingEnvironment processingEnv;

	public ServiceTypeVisitor(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	@Override
	public Boolean visitPackage(PackageElement e, Void p) {
		return true;
	}

	@Override
	public Boolean visitType(TypeElement e, Void p) {
		// 1.find whether interface.
		if (!e.getKind().isInterface())
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Messages.TYPE_MUST_BE_INTERFACE, e);
		// 2.find whether implements RpcService.
		List<? extends TypeMirror> interfaces = e.getInterfaces();
		if (!findServiceInterface(interfaces))
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Messages.TYPE_MUST_ANNOTATIONED_SERVICE, e);
		// 3.find protocol interfaces.
		
		return true;
	}

	private boolean findServiceInterface(List<? extends TypeMirror> interfaces) {
		for (TypeMirror typeMirror : interfaces) {
			if (typeMirror.toString()
					.equals(Messages.RPCSERVICE_INTERFACE_NAME))
				return true;
		}
		return false;
	}

	@Override
	public Boolean visitVariable(VariableElement e, Void p) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visitExecutable(ExecutableElement e, Void p) {
		this.processingEnv.getMessager().printMessage(Kind.ERROR,
				"method error", e);
		return true;
	}

	@Override
	public Boolean visitTypeParameter(TypeParameterElement e, Void p) {
		// TODO Auto-generated method stub
		return true;
	}
}
