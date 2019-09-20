package fi.lahtevanoja.keyvaluestore.service;

import static org.assertj.core.api.Assertions.assertThat;

import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import fi.lahtevanoja.keyvaluestore.repository.KeyValueRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Log4j2
public class KeyValueServiceTest {

  @MockBean
  private KeyValueRepository keyValueRepository;

  @BeforeEach
  public void reset() {
    Mockito.reset(keyValueRepository);
  }

  @AfterEach
  public void validate() {
    Mockito.validateMockitoUsage();
  }

  @Test
  public void updateOrCreateExistingKeyValue() {
    KeyValueService keyValueService = new KeyValueService(keyValueRepository);
    KeyValue keyValue = KeyValue.of("testkey", "testvalue");
    keyValue.setId(11L);
    KeyValue newKeyValue = KeyValue.of("testkey", "newtestvalue");

    ArgumentMatcher<KeyValue> keyValueMatcher = kv -> kv.equals(keyValue);

    Mockito.when(keyValueRepository.findByKey("testkey")).thenReturn(Optional.of(keyValue));
    Mockito.when(keyValueRepository.findById(11L)).thenReturn(Optional.of(keyValue));
    Mockito.doAnswer((InvocationOnMock invocation) -> null)
      .when(keyValueRepository)
      .updateKeyValue(Mockito.argThat(keyValueMatcher));

    KeyValue created = keyValueService.createOrUpdateKeyValue(newKeyValue);
    newKeyValue.setId(11L);
    assertThat(created)
      .isEqualTo(newKeyValue);

    Mockito.verify(keyValueRepository).findByKey("testkey");
  }
}
