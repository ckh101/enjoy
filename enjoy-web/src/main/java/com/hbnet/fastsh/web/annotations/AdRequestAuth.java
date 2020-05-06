package com.hbnet.fastsh.web.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AdRequestAuth {
    boolean isAuth() default true;
}
