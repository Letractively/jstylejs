package org.rayson.apt;

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
		SUPPORTED_ANNOTATION_TYPES.add(Constants.PROXY_ANNOTATION_NAME);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		// 1. find annotation of Protocols.
		TypeElement proxyTypeElement = getProxyAnnotationTypeElement(annotations);
		if (proxyTypeElement == null)
			return true;
		try {
			for (Element typeElement : roundEnv
					.getElementsAnnotatedWith(proxyTypeElement)) {
				// 2. find protocols annotation mirror.
				AnnotationMirror proxyAnnotationMirror = getProxyAnnotationMirror(typeElement);
				if (proxyAnnotationMirror == null)
					continue;
				AnnotationValue proxyAnnotation = proxyAnnotationMirror
						.getElementValues().values().iterator().next();
				typeElement.accept(new ServiceTypeVisitor(this.processingEnv,
						proxyAnnotation), proxyAnnotationMirror);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	private AnnotationMirror getProxyAnnotationMirror(Element element) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().toString()
					.equals(Constants.PROXY_ANNOTATION_NAME))
				return annotationMirror;
		}
		return null;
	}

	private static TypeElement getProxyAnnotationTypeElement(
			Set<? extends TypeElement> annotations) {
		for (TypeElement typeElement : annotations) {
			if (typeElement.getQualifiedName().contentEquals(
					Constants.PROXY_ANNOTATION_NAME))
				return typeElement;
		}
		return null;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}
}