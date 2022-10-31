package nl.rabo.accountinformation.exceptions.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private Integer code;
    private List<Violation> violations;
}