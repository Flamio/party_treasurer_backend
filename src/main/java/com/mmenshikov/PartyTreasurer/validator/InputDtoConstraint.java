
package com.mmenshikov.PartyTreasurer.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Created: 10.08.2022 11:31
 *
 * @author MMenshikov
 */
@Documented
@Constraint(validatedBy = InputDtoValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InputDtoConstraint {
  String message() default "Invalid dto";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
