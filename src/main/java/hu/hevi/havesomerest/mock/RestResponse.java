package hu.hevi.havesomerest.mock;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class RestResponse {

    private String id = "342";
    private String todo = "create a todo list";
    private List<String> tags = Arrays.asList("tag1", "tag2", "tag3");
    private List<SomethingInObject> objectArray = Arrays.asList(new SomethingInObject(), new SomethingInObject());
    private SomethingInObject somethingInObject = new SomethingInObject();

}
