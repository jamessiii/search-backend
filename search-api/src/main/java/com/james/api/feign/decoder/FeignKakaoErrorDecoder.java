package com.james.api.feign.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.core.exception.BadRequestException;
import com.james.core.exception.NoResponseFromServerException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class FeignKakaoErrorDecoder  implements ErrorDecoder {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 500:
                throw new NoResponseFromServerException();
            case 400:
                try {
                    String bodyString = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8)).lines()
                            .collect(Collectors.joining("\n"));
                    Map<String, String> resData = objectMapper.readValue(bodyString, Map.class);
                    throw new BadRequestException(resData.get("message"));

                } catch (IOException ignore) {
                    throw new BadRequestException();
                }
            default:
                return new Exception(response.reason());
        }
    }
}
