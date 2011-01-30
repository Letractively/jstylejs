package common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface ThreadSafe {
	boolean value() default true;
}
