package br.gov.mt.seplag.music_library_api.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${minio.url:http://localhost:9000}") String url,
            @Value("${minio.accessKey:root}") String accessKey,
            @Value("${minio.secretKey:rootpass123}") String secretKey
    ) {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
