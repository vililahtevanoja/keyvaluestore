DELETE FROM key_value WHERE key_value.value = NULL;

ALTER TABLE key_value
    ALTER COLUMN value SET NOT NULL;