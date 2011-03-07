package org.rayson.apt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import org.rayson.annotation.RpcService;

public class RpcServiceProcessor extends AbstractProcessor {

	private static final Set<String> SUPPORTED_ANNOTATION_TYPES;
	static {
		SUPPORTED_ANNOTATION_TYPES = new HashSet<String>();
		SUPPORTED_ANNOTATION_TYPES.add(RpcService.class.getName());

	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.unmodifiableSet(SUPPORTED_ANNOTATION_TYPES);
	}

}
