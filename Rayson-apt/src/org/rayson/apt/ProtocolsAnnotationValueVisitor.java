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

class ProtocolsAnnotationValueVisitor implements
		AnnotationValueVisitor<Boolean, AnnotationMirror> {

	private ProcessingEnvironment processingEnv;
	private TypeElement typeElement;

	ProtocolsAnnotationValueVisitor(ProcessingEnvironment processingEnv,
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
		// TODO Auto-generated method stub
		return null;
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
		for (AnnotationValue annotationValue : vals) {
			if (annotationValue.toString().equals(
					Constants.RPCSERVICE_INTERFACE_NAME))
				this.processingEnv.getMessager().printMessage(Kind.ERROR,
						Constants.PROTOCOL_MUST_EXTENDS_RPCPROTOCOL,
						this.typeElement, p);
		}
		return null;
	}

	@Override
	public Boolean visitUnknown(AnnotationValue av, AnnotationMirror p) {
		// TODO Auto-generated method stub
		return null;
	}

}
