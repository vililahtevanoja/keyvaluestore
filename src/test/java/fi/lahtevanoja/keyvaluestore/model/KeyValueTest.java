package fi.lahtevanoja.keyvaluestore.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class KeyValueTest {

  @Test
  public void isValidIfHasKeyAndValue() {
    KeyValue testKeyValue = KeyValue.of("key", "value");
    assertThat(testKeyValue.isValid())
      .isTrue();
  }

  @Test
  public void isNotValidIfHasValueButNoKey() {
    KeyValue testKeyValue = KeyValue.of(null, "value");
    assertThat(testKeyValue.isValid())
      .isFalse();
  }

  @Test
  public void isEmptyIfNullKeyAndValue() {
    KeyValue testKeyValue = KeyValue.of(null, null);
    assertThat(testKeyValue.isEmpty())
      .isTrue();
  }

  @Test
  public void isEmptyIfBlankKeyAndValue() {
    KeyValue testKeyValue = KeyValue.of(" ", " ");
    assertThat(testKeyValue.isEmpty())
      .isTrue();
  }
}
