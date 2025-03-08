
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IdentifierNumberValidator.class)
@Target(ElementType.TYPE) // Se aplica a la clase Leg en lugar del campo
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentifierNumber {

	String message() default "{placeholder}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
