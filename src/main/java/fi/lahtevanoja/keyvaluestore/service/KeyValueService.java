package fi.lahtevanoja.keyvaluestore.service;

import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import fi.lahtevanoja.keyvaluestore.model.KeyValueUpdate;
import fi.lahtevanoja.keyvaluestore.repository.KeyValueRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class KeyValueService {

  private final KeyValueRepository keyValueRepository;

  public Iterable<KeyValue> getKeyValues() {
    return this.keyValueRepository.findAll();
  }

  public Optional<KeyValue> getKeyValue(Long id) {
    return this.keyValueRepository.findById(id);
  }

  public Optional<KeyValue> getKeyValueByKey(String key) {
    return this.keyValueRepository.findByKey(key);
  }

  public KeyValue createKeyValue(KeyValue keyValue) {
    KeyValue created = this.keyValueRepository.save(keyValue);
    log.debug("KeyValue created: {}", created);
    return created;
  }

  public void deleteKeyValue(Long id) {
    this.keyValueRepository.deleteById(id);
  }

  public Optional<KeyValue> updateKeyValue(Long id, KeyValueUpdate keyValueUpdate) {
    Optional<KeyValue> oldKeyValue = this.getKeyValue(id);
    if (oldKeyValue.isEmpty()) {
      return Optional.empty();
    }
    KeyValue newKeyValue = KeyValue.of(oldKeyValue.get().getKey(), keyValueUpdate.getValue());
    newKeyValue.setId(oldKeyValue.get().getId());
    this.keyValueRepository.updateKeyValue(newKeyValue);
    log.debug("KeyValue updated: {}", newKeyValue);
    return Optional.of(newKeyValue);
  }

  public KeyValue createOrUpdateKeyValue(KeyValue keyValue) {
    Optional<KeyValue> existing = this.keyValueRepository.findByKey(keyValue.getKey());
    log.debug("Creating or updating KeyValue");
    if (existing.isPresent()) {
      Long existingId = existing.get().getId();
      log.debug("KeyValue with key exists: {}", existing.get());
      log.debug("Updating KeyValue with new value");
      Optional<KeyValue> updated = this.updateKeyValue(existingId, KeyValueUpdate.of(keyValue));
      if (updated.isPresent()) {
        log.debug("Updated KeyValue: {}", updated.get());
        return updated.get();
      } else {
        log.error("Well this should not have happened, how did we end here?");
        throw new RuntimeException("Codepath reached that should not have been reachable");
      }
    } else {
      log.debug("KeyValue with key did not exist, creating new");
      KeyValue created = this.createKeyValue(keyValue);
      log.debug("Created new KeyValue: {}", created);
      return created;
    }
  }
}
