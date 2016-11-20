package hu.hevi.havesomerest.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UrlMappingProvider {

    @Autowired
    private AppInitializer appInitializer;

    public Map<String, Object> getUrlMap() {
        return new HashMap<>(appInitializer.getUrlMap());
    }
}
