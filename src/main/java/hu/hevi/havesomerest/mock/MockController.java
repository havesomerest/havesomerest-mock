package hu.hevi.havesomerest.mock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

    @RequestMapping("/todo/342")
    public RestResponse getResponse() {
        return new RestResponse();
    }
}
