package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class MockRequestHandler extends ResourceHttpRequestHandler {

    private static final boolean VERBOSE = false;

    private RequestRepository requestRepository;

    @Getter
    private List<Test> tests = new ArrayList<>();

    public MockRequestHandler(RequestRepository requestRepository, Test test) {
        this.requestRepository = requestRepository;
        this.tests.add(test);
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("\n" + LocalDateTime.now() + "\n" + request.getRequestURI() + "\n");

        Map<String, String> headerByHeaderName = getHeadersByHeaderName(request);

        String requestBody = "";
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            requestBody = request.getReader()
                                 .lines()
                                 .collect(Collectors.joining(System.lineSeparator()));
        }

        String uri = tests.get(0).getEndpointParts()
                          .stream()
                          .map(endpointPart -> endpointPart.toString())
                          .collect(Collectors.joining("/"));

        AcceptedRequest acceptedRequest = AcceptedRequest.builder()
                                                         .uri(uri)
                                                         .requestHeaders(headerByHeaderName)
                                                         .requestBody(requestBody)
                                                         .build();
        requestRepository.save(acceptedRequest);

        List<Test> testsMatchedMethod = this.tests.stream()
                                                  .filter(t -> t.getMethod().equals(HttpMethod.valueOf(request.getMethod())))
                                                  .collect(Collectors.toList());

        if (testsMatchedMethod.size() > 0) {
            int testToRun = 0;
            if (testsMatchedMethod.size() > 1) {
                testToRun = ThreadLocalRandom.current().nextInt(0, testsMatchedMethod.size() - 1);
            }
            Test test = testsMatchedMethod.get(testToRun);



            if (test.getResponseHeaders() != null) {
                test.getResponseHeaders().keySet().forEach((key) -> {
                    response.setHeader(key, test.getResponseHeaders().get(key).get(0));
                });
            }

            switch (test.getFileType()) {
                case XML:
                    response.setContentType("application/xml");
                    break;
                case JSON:
                    response.setContentType("application/json");
                    break;
            }
            response.setStatus(Integer.valueOf(test.getStatusCode()));
            PrintWriter out = response.getWriter();
            if (test.hasResponse()) {
                out.print(test.getResponse().toString());
            }

            out.flush();
        }
    }

    private Map<String, String> getHeadersByHeaderName(HttpServletRequest request) {
        Map<String, String> headerByHeaderName = new HashMap<>();
        Enumeration requestHeaderNames = request.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = (String) requestHeaderNames.nextElement();
            String header = request.getHeader(headerName);
            headerByHeaderName.put(headerName, header);
            if (VERBOSE) {
                log.info(headerName + ": " + header);
            }
        }
        return headerByHeaderName;
    }

    public void addTest(Test test) {
        this.tests.add(test);
    }
}
