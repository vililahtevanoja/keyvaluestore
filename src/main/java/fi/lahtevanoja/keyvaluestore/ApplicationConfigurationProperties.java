package fi.lahtevanoja.keyvaluestore;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("keyvaluestore")
@Data
public class ApplicationConfigurationProperties {
  private final List<String> restrictedKeyValues;
}
