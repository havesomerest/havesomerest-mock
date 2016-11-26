package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.common.EndPointNameBuilder;
import hu.hevi.havesomerest.converter.ToTestConverter;
import hu.hevi.havesomerest.io.StructureReader;
import hu.hevi.havesomerest.io.TestDirectory;
import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void initApplicationContext() throws BeansException {
        Map<String, Object> urlMap = (Map<String, Object>) this.getUrlMap();

        Map<Path, Optional<TestDirectory>> filesByDirectory = null;
        try {
            filesByDirectory = structureReader.getStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Test, String> tests = toTestConverter.convert(filesByDirectory);

        tests.keySet().forEach(test -> {
            String endPoint = endPointNameBuilder.build(test.getEndpointParts());
            if (urlMap.containsKey(endPoint)) {
                MockRequestHandler requestHandler = (MockRequestHandler) urlMap.get(endPoint);
                requestHandler.addTest(test);
            } else {
                urlMap.put(endPoint, new MockRequestHandler(requestRepository, test));
            }
        });

        this.registerHandlers(urlMap);

    }
}
