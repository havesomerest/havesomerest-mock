package hu.hevi.havesomerest.mock;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AcceptedRequest {

    private Map<String, String> requestHeaders;
    private String requestBody;
}
