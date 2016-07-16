package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.common.EndPointNameBuilder;
import hu.hevi.havesomerest.converter.ToTestConverter;
import hu.hevi.havesomerest.io.StructureReader;
import hu.hevi.havesomerest.io.TestDirectory;
import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AppInitializer extends SimpleUrlHandlerMapping {

    @Autowired
    private StructureReader structureReader;
    @Autowired
    private ToTestConverter toTestConverter;
    @Autowired
    private EndPointNameBuilder endPointNameBuilder;

    @Override
    public void initApplicationContext() throws BeansException {
        System.out.println("GETHANDLER");
        Map<String, Object> urlMap = (Map<String, Object>) this.getUrlMap();

        Map<Path, Optional<TestDirectory>> filesByDirectory = null;
        try {
            filesByDirectory = structureReader.getStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Test, JSONObject> tests = toTestConverter.convert(filesByDirectory);

        tests.keySet().forEach(test -> {
            log.info(test.toString());
            String endPoint = endPointNameBuilder.build(test);
            log.info("endpoint:::: " + endPoint);
            urlMap.put(endPoint, new MockRequestHandler(test));
        });

        this.registerHandlers(urlMap);
    }
}
