package org.creativor.rayson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.creativor.rayson.api.RpcProxy;

/**
 * Annotation a {@link RpcProxy} version number.
 * 
 * @author hp
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyVersion {
	public static short DEFAULT_VALUE = 0;

	public short value() default DEFAULT_VALUE;
}