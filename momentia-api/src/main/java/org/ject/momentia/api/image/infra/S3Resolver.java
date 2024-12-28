package org.ject.momentia.api.image.infra;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Resolver {
	private static final Duration PUT_IMAGE_DURATION = Duration.of(10, ChronoUnit.MINUTES);

	@Value("${s3.bucketName}")
	private String bucketName;

	private final S3Operations s3Operations;

	public String startImageUpload(String imageKey, Long expectedSize, String contentType) {
		var metadata = ObjectMetadata.builder()
			.contentLength(expectedSize)
			.contentType(contentType)
			.build();

		return s3Operations.createSignedPutURL(bucketName, imageKey, PUT_IMAGE_DURATION, metadata, null)
			.toString();
	}
}
