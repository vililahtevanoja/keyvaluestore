package fi.lahtevanoja.keyvaluestore;

import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import fi.lahtevanoja.keyvaluestore.repository.KeyValueRepository;
import fi.lahtevanoja.keyvaluestore.service.KeyValueService;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public abstract class KeyValueStoreIntegrationTest {

  @Autowired
  private KeyValueService keyValueService;

  @Autowired
  private KeyValueRepository keyValueRepository;

  protected KeyValue generateKeyValue() {
    return KeyValue.of(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
  }

  protected KeyValue createKeyValue() {
    KeyValue kv = generateKeyValue();
    return keyValueService.createKeyValue(kv);
  }

  protected KeyValue insertKeyvalue(KeyValue kv) {
    return keyValueService.createKeyValue(kv);
  }

  protected void clearDatabase() {
    this.keyValueService.getKeyValues()
      .forEach(kv -> keyValueService.deleteKeyValue(kv.getId()));
  }

  protected void flush() {
    this.keyValueRepository.flush();
  }

}
