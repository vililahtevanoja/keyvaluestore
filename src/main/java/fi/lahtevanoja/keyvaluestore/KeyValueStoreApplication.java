package fi.lahtevanoja.keyvaluestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableConfigurationProperties
@EnableWebMvc
public class KeyValueStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(KeyValueStoreApplication.class, args);
  }

}
