package com.james.api.feign.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeISO8601Deserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        return LocalDateTime.from(
                    Instant.from(
                            DateTimeFormatter.ISO_DATE_TIME.parse(p.getText())
                    ).atZone(ZoneId.of("Asia/Seoul"))
            );
    }
}
