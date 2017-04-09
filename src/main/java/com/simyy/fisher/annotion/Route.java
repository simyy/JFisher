package com.simyy.fisher.annotion;

import java.lang.annotation.*;

/**
 * mark router
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {
    String value() default "";
    String desc() default "";
}
