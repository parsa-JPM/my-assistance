package ir.codefather.assistance.command.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * it defines flag of command function it must be used with CommandFun annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {
    String name();

    String description();

    boolean withValue() default false;
}
