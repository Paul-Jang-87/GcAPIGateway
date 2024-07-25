package gc.apiClient.customproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "custom-properties")

/**
 * 이 클래스의 역할은
 * 첫째 로직을 구분해 주는 디비전 값을 'application.properties' 파일에서 가지고 오는 것
 * 둘째 발신결과 코드 값들을 'application.properties' 파일에서 가지고 오는 것.
 * 
 * 하지만 첫번째 기능은 사용하지 않는다. 값을 'application.properties' 파일에서 가지고 오지 않고 그냥 소스코드에 하드코딩으로 값을 넣어주는 것으로 바꿨다.
 * 따라서 두번째 기능만을 사용한다. 
 * 
 */


public class CustomProperties {//application.properties에서 custom-properties과 관련된 설정들을 다루는 매핑 클래스.  

    private Map<String, String> properties = new HashMap<>();
    private Map<String, String> division = new HashMap<>();

    public Map<String, String> getProperties() {
        return properties;
    }
    
    public Map<String, String> getDivision() {
        return division;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
    
    public void setDivision(Map<String, String> division) {
        this.division = division;
    }
}

