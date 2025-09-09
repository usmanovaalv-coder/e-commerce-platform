package com.ecommerce.productservice.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.s3")
public record AwsS3Property(String accessKey, String secretKey, String region, String bucketName) {
}
