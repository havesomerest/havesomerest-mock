package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MockRequestHandler extends ResourceHttpRequestHandler {

    private List<Test> tests = new ArrayList<>();

    public MockRequestHandler(Test test) {
        this.tests.add(test);
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Test> testsMatchedMethod = this.tests.stream()
                                                  .filter(t -> t.getMethod().equals(HttpMethod.valueOf(request.getMethod())))
                                                  .collect(Collectors.toList());

        if (testsMatchedMethod.size() > 0) {
            Test test = testsMatchedMethod.get(0);



            test.getResponseHeaders().keySet().forEach((key) -> {
                response.setHeader(key, test.getResponseHeaders().get(key).get(0));
            });
            response.setContentType("application/json");
            response.setStatus(Integer.valueOf(test.getStatusCode()));
            PrintWriter out = response.getWriter();
            if (test.hasResponse()) {
                out.print(test.getResponse().toString());
            }

            out.flush();
        }
    }

    public void addTest(Test test) {
        this.tests.add(test);
    }
}
