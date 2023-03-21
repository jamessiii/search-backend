package com.james.api.feign.decoder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.james.api.feign.deserializer.DateTimeDeserializer;
import feign.Response;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class FeignNaverDecoder implements Decoder {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .registerModule(new SimpleModule()
                    .addDeserializer(LocalDateTime.class, new DateTimeDeserializer()))
            .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

    @Override
    public Object decode(Response response, Type type) throws IOException {

        String bodyString = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        Map<String, Object> data = objectMapper.readValue(bodyString, Map.class);
        return objectMapper.convertValue(data, (Class<?>)type);
    }
}
