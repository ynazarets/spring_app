package mate.academy.app.dto.user.reg;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import mate.academy.app.security.validator.PasswordValidate;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@PasswordValidate(
        password = "password",
        confirmPassword = "repeatPassword",
        message = "Passwords do not match, try again."
)
public class UserRegistrationRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 55)
    private String password;

    @NotBlank
    @Length(min = 8, max = 55)
        private String repeatPassword;

    @NotBlank
    @Length(max = 55)
    private String firstName;

    @NotBlank
    @Length(max = 55)
    private String lastName;

    @Length(max = 128)
    private String shippingAddress;
}
