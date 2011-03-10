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

	private static final Set<String> SUPPORTED_ANNOTATION_TYPES;
	static {
		SUPPORTED_ANNOTATION_TYPES = new HashSet<String>();
		SUPPORTED_ANNOTATION_TYPES.add(Messages.PROTOCOLS_ANNOTATION_NAME);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		// 1. find annotation of Protocols.
		TypeElement protocleTypeElement = getProtocolsAnnotationTypeElement(annotations);
		if (protocleTypeElement == null)
			return false;
		try {
			for (Element typeElement : roundEnv
					.getElementsAnnotatedWith(protocleTypeElement)) {
				typeElement.accept(new ServiceTypeVisitor(this.processingEnv),
						null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static TypeElement getProtocolsAnnotationTypeElement(
			Set<? extends TypeElement> annotations) {
		for (TypeElement typeElement : annotations) {
			if (typeElement.getQualifiedName().contentEquals(
					Messages.PROTOCOLS_ANNOTATION_NAME))
				return typeElement;
		}
		return null;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}
}