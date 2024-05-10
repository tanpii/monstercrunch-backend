package edu.mirea.cookie_shop.configuration.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "minio", ignoreUnknownFields = false)
@Configuration
@Data
public class MinioProperties {
    private String bucket;
    private String comment;
    private String url;
    private String accessKey;
    private String secretKey;
}
