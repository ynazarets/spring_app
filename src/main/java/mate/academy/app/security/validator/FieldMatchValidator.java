package mate.academy.app.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.academy.app.dto.user.UserRegistrationRequestDto;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator
        implements ConstraintValidator<PasswordValidate, UserRegistrationRequestDto> {
    private String password;
    private String confirmPassword;
    private String message;

    @Override
    public void initialize(PasswordValidate constraintAnnotation) {
        password = constraintAnnotation.password();
        confirmPassword = constraintAnnotation.confirmPassword();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        try {
            final Object firstObj = new BeanWrapperImpl(userRegistrationRequestDto)
                    .getPropertyValue(password);

            final Object secondObj = new BeanWrapperImpl(userRegistrationRequestDto)
                    .getPropertyValue(confirmPassword);

            boolean isValid = Objects.equals(firstObj, secondObj);

            if (!isValid) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(confirmPassword)
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }
            return isValid;

        } catch (final Exception e) {
            return false;
        }
    }
}
