package fi.lahtevanoja.keyvaluestore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.lahtevanoja.keyvaluestore.KeyValueStoreIntegrationTest;
import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class KeyValueControllerTest extends KeyValueStoreIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @AfterEach
  public void tearDown() {
    clearDatabase();
  }

  @Test
  public void testGetKeyValues() throws Exception {
    // Setup
    KeyValue kv1 = createKeyValue();
    KeyValue kv2 = createKeyValue();

    MvcResult result = mockMvc.perform(get("/api/keyvalues/"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andReturn();

    List<KeyValue> keyValues = objectMapper
      .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<KeyValue>>() {
      });
    assertThat(keyValues)
      .hasSize(1)
      .anyMatch(kv -> KeyValue.areEqual(kv, kv1))
      .anyMatch(kv -> KeyValue.areEqual(kv, kv2));
  }

  @Test
  public void testGetByKey() throws Exception {
    // Setup
    createKeyValue();
    KeyValue kv2 = createKeyValue();

    mockMvc.perform(get("/api/keyvalues/bykey")
      .param("key", kv2.getKey()))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$.key").value(kv2.getKey()))
      .andExpect(jsonPath("$.value").value(kv2.getValue()));
  }

  @Test
  public void testPostKey() throws Exception {
    KeyValue kv = generateKeyValue();
    mockMvc.perform(post("/api/keyvalues").contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(objectMapper.writeValueAsString(kv)))
      .andExpect(status().isCreated());
  }

  @Test
  public void updatesKeyValueOnPostingAnExistingKey() throws Exception {
    KeyValue kv = createKeyValue();
    flush();
    KeyValue updatedKv = KeyValue.of(kv.getKey(), "newValue");
    mockMvc.perform(
        post("/api/keyvalues")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(updatedKv)))
      .andExpect(status().isCreated())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$.id").value(kv.getId()))
      .andExpect(jsonPath("$.key").value(kv.getKey()))
      .andExpect(jsonPath("$.value").value(updatedKv.getValue()));
  }

  @Test
  public void testReturnsBadRequestOnBlankValue() throws Exception {
    KeyValue kv = KeyValue.of("  ", "abc");
    mockMvc.perform(post("/api/keyvalues").contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(objectMapper.writeValueAsString(kv)))
      .andExpect(status().isBadRequest());
  }
}
