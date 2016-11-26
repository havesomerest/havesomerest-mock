package hu.hevi.havesomerest.mock;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@ToString
public class AcceptedRequest {

    private String uri;
    private Map<String, String> requestHeaders;
    private String requestBody;
}
