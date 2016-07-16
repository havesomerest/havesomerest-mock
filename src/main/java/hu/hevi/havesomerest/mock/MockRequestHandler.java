package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

@Slf4j
public class MockRequestHandler extends ResourceHttpRequestHandler {

    private Test test;

    public MockRequestHandler(Test test) {
        this.test = test;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RequestMethod.POST.name().equals(request.getMethod())) {
            System.out.println(StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset()));
        }

        System.out.println("HANDLEREQUESTINTERNAL " + request.getServletPath());

        test.getResponseHeaders().keySet().forEach((key) -> {
            System.out.println("KEEEEEEEY::::: " + key.toString());
            response.setHeader(key, test.getResponseHeaders().get(key).get(0));
        });
        response.setContentType("application/json");
        response.setStatus(Integer.valueOf(test.getStatusCode()));
        PrintWriter out = response.getWriter();
        System.out.println(test.getResponse().toString());
        out.print(test.getResponse().toString());
        out.flush();
    }
}
