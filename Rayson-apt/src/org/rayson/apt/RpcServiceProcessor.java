package org.rayson.apt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RpcServiceProcessor extends AbstractProcessor {
	public RpcServiceProcessor() {

	}

	private static final Set<String> SUPPORTED_ANNOTATION_TYPES;
	static {
		SUPPORTED_ANNOTATION_TYPES = new HashSet<String>();
		SUPPORTED_ANNOTATION_TYPES.add(Constants.PROTOCOLS_ANNOTATION_NAME);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		// 1. find annotation of Protocols.
		TypeElement protocolsTypeElement = getProtocolsAnnotationTypeElement(annotations);
		if (protocolsTypeElement == null)
			return false;
		try {
			for (Element typeElement : roundEnv
					.getElementsAnnotatedWith(protocolsTypeElement)) {
				AnnotationMirror protocolsAnnotationMirror = getProtocolsAnnotationMirror(typeElement);
				if (protocolsAnnotationMirror == null)
					continue;
				AnnotationValue protocolsClasses = protocolsAnnotationMirror
						.getElementValues().values().iterator().next();
				System.out.println(protocolsClasses.toString());
				typeElement.accept(new ServiceTypeVisitor(this.processingEnv,
						protocolsClasses), protocolsAnnotationMirror);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private AnnotationMirror getProtocolsAnnotationMirror(Element element) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().toString()
					.equals(Constants.PROTOCOLS_ANNOTATION_NAME))
				return annotationMirror;
		}
		return null;
	}

	private static TypeElement getProtocolsAnnotationTypeElement(
			Set<? extends TypeElement> annotations) {
		for (TypeElement typeElement : annotations) {
			if (typeElement.getQualifiedName().contentEquals(
					Constants.PROTOCOLS_ANNOTATION_NAME))
				return typeElement;
		}
		return null;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}
}