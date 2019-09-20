package fi.lahtevanoja.keyvaluestore.controller;

import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import fi.lahtevanoja.keyvaluestore.model.KeyValueUpdate;
import fi.lahtevanoja.keyvaluestore.service.KeyValueService;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KeyValueController {

  private final KeyValueService keyValueService;

  @GetMapping(value = "/api/keyvalues", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Iterable<KeyValue>> getAllKeyValues() {
    return ResponseEntity.ok(this.keyValueService.getKeyValues());
  }

  @PostMapping(value = "/api/keyvalues",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<KeyValue> createKeyValue(@RequestBody KeyValue keyValue) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .body(this.keyValueService.createOrUpdateKeyValue(keyValue));
  }

  @GetMapping(value = "/api/keyvalues/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Optional<KeyValue> getKeyValue(@PathVariable("id") Long id) {
    return this.keyValueService.getKeyValue(id);
  }

  @PatchMapping(value = "/api/keyvalues/{id}")
  public ResponseEntity<KeyValue> updateKeyValue(@PathVariable("id") Long id,
    @RequestBody KeyValueUpdate keyValueUpdate) {
    return ResponseEntity.of(this.keyValueService.updateKeyValue(id, keyValueUpdate));
  }

  @GetMapping("/api/keyvalues/bykey")
  public Optional<KeyValue> getKeyValueByKey(
    @RequestParam(name = "key", required = true) String key) {
    return this.keyValueService.getKeyValueByKey(key);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public void constraintViolationException(ConstraintViolationException ex,
    HttpServletResponse response) throws IOException {
    String msg = ex.getConstraintViolations().stream()
      .map(cv -> "'" + cv.getPropertyPath() + "' " + cv.getMessage())
      .collect(Collectors.joining("\n"));
    response.sendError(HttpStatus.BAD_REQUEST.value(), msg);
  }
}
