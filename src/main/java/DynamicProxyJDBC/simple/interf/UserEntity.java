package DynamicProxyJDBC.simple.interf;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEntity {
    String name() default "";
}
