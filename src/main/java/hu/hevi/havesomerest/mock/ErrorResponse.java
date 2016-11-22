package hu.hevi.havesomerest.mock;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ErrorResponse {

    private String errorMessage;
}
