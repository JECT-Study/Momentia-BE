package org.ject.momentia.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties("s3")
public record S3Property(
	@NotEmpty String region,
	@NotEmpty String endpoint,
	@NotEmpty String accessKey,
	@NotEmpty String secretKey,
	@NotEmpty String bucketName
) {
}
