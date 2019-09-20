package fi.lahtevanoja.keyvaluestore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Data
public class KeyValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotEmpty
  @NotBlank
  @Column(unique = true)
  private final String key;

  @NotNull
  private final String value;

  public static KeyValue of(String key, String value) {
    return new KeyValue(key, value);
  }

  public static boolean areEqual(KeyValue kv1, KeyValue kv2) {
    return StringUtils.equals(kv1.getKey(), kv2.getKey())
      && StringUtils.equals(kv1.getValue(), kv2.getValue());
  }

  @JsonIgnore
  public boolean isEmpty() {
    return StringUtils.isBlank(this.key) && StringUtils.isBlank(this.value);
  }

  @JsonIgnore
  public boolean isValid() {
    return !isEmpty() && StringUtils.isNotBlank(this.key);
  }
}
