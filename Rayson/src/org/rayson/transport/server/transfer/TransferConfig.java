package org.rayson.transport.server.transfer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.rayson.annotation.Config;

@Config
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface TransferConfig {
	public int workerCount() default 4;
}
