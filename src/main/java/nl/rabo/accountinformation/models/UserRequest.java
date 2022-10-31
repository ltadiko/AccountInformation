package nl.rabo.accountinformation.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.UserStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    @NotEmpty
    @Size(min = 2, message = "First name is too short")
    @Size(max = 40, message = "First name is too long")
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotNull
    private UserStatus status;
    @NotEmpty
    private String email;
}
