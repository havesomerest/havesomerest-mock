package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UrlMappingRepostitory {

    @Autowired
    private UrlMappingProvider urlMappingProvider;

    public Optional<List<Test>> getUrlMapping(String urlMapping) {
        Optional<List<Test>> result = Optional.empty();

        Map<String, Object> urlMap = urlMappingProvider.getUrlMap();
        MockRequestHandler requestHandler = (MockRequestHandler) urlMap.get(urlMapping);
        if (requestHandler != null) {
            List<Test> tests = requestHandler.getTests();
            result = Optional.of(tests);
        }
        return result;
    }
}
