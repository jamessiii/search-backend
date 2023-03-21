package com.james.api.feign.configuration;

import com.james.api.feign.decoder.FeignNaverDecoder;
import com.james.api.feign.decoder.FeignNaverErrorDecoder;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class SearchNaverConfig {

    @Bean
    public ErrorDecoder naverErrorDecoder() {
        return new FeignNaverErrorDecoder();
    }

    @Bean
    public Decoder naverDecoder() {
        return new FeignNaverDecoder();
    }
}
