package org.rayson.apt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.AbstractElementVisitor6;

public class RpcServiceProcessor extends AbstractProcessor {
	public RpcServiceProcessor() {

	}

	private static final String PROTOCOLS__ANNOTATION_NAME = "org.rayson.annotation.RpcProtocols";

	private static final Set<String> SUPPORTED_ANNOTATION_TYPES;
	static {
		SUPPORTED_ANNOTATION_TYPES = new HashSet<String>();
		SUPPORTED_ANNOTATION_TYPES.add(PROTOCOLS__ANNOTATION_NAME);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		try {
			Set<? extends TypeElement> rpcServiceTypes = (Set<? extends TypeElement>) roundEnv
					.getElementsAnnotatedWith(annotations.iterator().next());
			for (TypeElement rpcServiceType : rpcServiceTypes) {
				rpcServiceType.accept();
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}

}
