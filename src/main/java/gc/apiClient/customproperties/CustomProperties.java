package gc.apiClient.customproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "custom-properties")
public class CustomProperties {//application.properties에서 custom-properties과 관련된 설정들을 다루는 매핑 클래스.  

    private Map<String, String> properties = new HashMap<>();
//    private Map<String, String> pp = new HashMap<>();

    public Map<String, String> getProperties() {
        return properties;
    }
    
//    public Map<String, String> getpp() {
//        return pp;
//    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
    
//    public void setpp(Map<String, String> pp) {
//        this.pp = pp;
//    }
}

