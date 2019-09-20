package fi.lahtevanoja.keyvaluestore;

import static org.assertj.core.api.Assertions.assertThat;

import fi.lahtevanoja.keyvaluestore.controller.KeyValueController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class KeyValueStoreApplicationTest {

  @Autowired
  private KeyValueController keyValueController;

  @Test
  public void contextLoads() {
    assertThat(keyValueController)
      .isNotNull();
  }

}
