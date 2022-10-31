package nl.rabo.accountinformation.exceptions.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class Violation {
    private String fieldName;
    private String message;
}
