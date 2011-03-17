package org.rayson.apt;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

class ProxyAnnotationValueVisitor implements
		AnnotationValueVisitor<Boolean, AnnotationMirror> {

	private ProcessingEnvironment processingEnv;
	private TypeElement typeElement;

	ProxyAnnotationValueVisitor(ProcessingEnvironment processingEnv,
			TypeElement typeElement) {
		this.processingEnv = processingEnv;
		this.typeElement = typeElement;
	}

	@Override
	public Boolean visit(AnnotationValue av, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(AnnotationValue av) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitBoolean(boolean b, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitByte(byte b, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitChar(char c, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitDouble(double d, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitFloat(float f, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitInt(int i, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitLong(long i, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitShort(short s, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitString(String s, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitType(TypeMirror t, AnnotationMirror p) {
		// here t is the rpc proxy.
		if (t.toString().equals(Constants.PROXY_INTERFACE_NAME)) {
			this.processingEnv.getMessager().printMessage(Kind.ERROR,
					Constants.PROXY_MUST_BE_INTERFACE, this.typeElement, p);
			return false;
		}
		return true;
	}

	@Override
	public Boolean visitEnumConstant(VariableElement c, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitAnnotation(AnnotationMirror a, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visitArray(List<? extends AnnotationValue> vals,
			AnnotationMirror p) {
		// for (AnnotationValue annotationValue : vals) {
		// if (annotationValue.toString().equals(
		// Constants.PROXY_INTERFACE_NAME)) {
		// this.processingEnv.getMessager().printMessage(Kind.ERROR,
		// Constants.SERVICE_MUST_EXTENDS_RPCPROTOCOL,
		// this.typeElement, p, annotationValue);
		// return false;
		// }
		// }
		return true;
	}

	@Override
	public Boolean visitUnknown(AnnotationValue av, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

}
