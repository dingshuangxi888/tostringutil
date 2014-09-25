package net.locplus.tools;

import java.lang.annotation.*;

/**
 * Created by Dean on 2014/9/24.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ToString {
    /**
     * ToString类型
     * @return
     */
    ToStringType value() default ToStringType.SHORT;
}
