package fi.lahtevanoja.keyvaluestore.repository;

import fi.lahtevanoja.keyvaluestore.model.KeyValue;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "key_value", path = "/api/reporest")
public interface KeyValueRepository extends JpaRepository<KeyValue, Long> {

  Optional<KeyValue> findByKey(@Param("key") String key);

  boolean existsByKey(String key);

  @Transactional
  @Modifying
  @Query("UPDATE KeyValue kv SET kv.value = :#{#keyValue.value} WHERE kv.key = :#{#keyValue.key}")
  void updateKeyValue(@Param("keyValue") KeyValue keyValue);

}
