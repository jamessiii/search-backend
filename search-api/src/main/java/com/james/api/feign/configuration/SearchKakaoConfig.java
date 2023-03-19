package com.james.api.feign.configuration;

import com.james.api.feign.decoder.FeignKakaoDecoder;
import com.james.api.feign.decoder.FeignKakaoErrorDecoder;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class SearchKakaoConfig {

    @Bean
    public ErrorDecoder kakapErrorDecoder() {
        return new FeignKakaoErrorDecoder();
    }

    @Bean
    public Decoder kakaoDecoder() {
        return new FeignKakaoDecoder();
    }
}
