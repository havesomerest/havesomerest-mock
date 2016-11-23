package hu.hevi.havesomerest.mock;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AcceptedRequest {

    private String uri;
    private Map<String, String> requestHeaders;
    private String requestBody;
}
