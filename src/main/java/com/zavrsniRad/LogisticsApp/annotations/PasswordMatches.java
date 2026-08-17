package com.zavrsniRad.LogisticsApp.annotations;


import com.zavrsniRad.LogisticsApp.constraintValidators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to validate that two password fields in a DTO match.
 * <p>
 * Intended for use on classes that have both a {@code password} and
 * a {@code confirmPassword} field.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 *     @PasswordMatches
 *     public class SignUpRequest{
 *          private String password;
 *          private String confirmPassword;
 *     }
 *    }
 * </pre>
 */

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {

    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
