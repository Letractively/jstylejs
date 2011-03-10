package org.rayson.apt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RpcServiceProcessor extends AbstractProcessor {
	public RpcServiceProcessor() {

	}

	private static final String PROTOCOLS_ANNOTATION_NAME = "org.rayson.annotation.Protocols";

	private static final Set<String> SUPPORTED_ANNOTATION_TYPES;
	static {
		SUPPORTED_ANNOTATION_TYPES = new HashSet<String>();
		SUPPORTED_ANNOTATION_TYPES.add(PROTOCOLS_ANNOTATION_NAME);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		try {
			for (Element typeElement : getElementsAnnotationedWithService(
					annotations, roundEnv)) {
				System.out.println(typeElement);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private Set<? extends Element> getElementsAnnotationedWithService(
			Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		TypeElement typeElement = getServiceAnnotationTypeElement(annotations);
		if (typeElement == null)
			return Collections.EMPTY_SET;
		else
			return roundEnv.getElementsAnnotatedWith(typeElement);
	}

	private TypeElement getServiceAnnotationTypeElement(
			Set<? extends TypeElement> annotations) {
		for (TypeElement typeElement : annotations) {
			if (typeElement.getQualifiedName().contentEquals(
					PROTOCOLS_ANNOTATION_NAME))
				return typeElement;
		}
		return null;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}

}
