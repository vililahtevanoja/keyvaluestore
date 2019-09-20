package fi.lahtevanoja.keyvaluestore.model;

import lombok.Data;

@Data
public class KeyValueUpdate {
  private final String value;

  public static KeyValueUpdate of(KeyValue keyValue) {
    return new KeyValueUpdate(keyValue.getValue());
  }

  public static KeyValueUpdate of(String value) {
    return new KeyValueUpdate(value);
  }
}
