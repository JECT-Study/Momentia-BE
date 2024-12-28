package org.ject.momentia.api.image.infra;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.ject.momentia.api.config.property.S3Property;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3Resolver {
	private static final Duration PUT_IMAGE_DURATION = Duration.of(10, ChronoUnit.MINUTES);
	private final S3Property s3Property;
	private final StaticCredentialsProvider staticCredentialsProvider;

	public S3Resolver(S3Property s3Property) {
		this.s3Property = s3Property;
		this.staticCredentialsProvider = StaticCredentialsProvider.create(
			AwsBasicCredentials.create(s3Property.accessKey(), s3Property.secretKey())
		);
	}

	public Pair<String, String> startImageUpload(String imageKey, Long expectedSize, String contentType) {
		var putUrl = createPresignedPutUrl(imageKey, expectedSize, contentType);
		var getUrl = createPresignedGetUrl(imageKey);

		return Pair.of(putUrl, getUrl);
	}

	private String createPresignedPutUrl(String imageKey, Long expectedSize, String contentType) {
		try (var s3Presigner = createS3Presigner()) {
			var putObjectRequest = PutObjectRequest.builder()
				.bucket(s3Property.bucketName())
				.key(imageKey)
				.contentLength(expectedSize)
				.contentType(contentType)
				.build();

			var presignedRequest = PutObjectPresignRequest.builder()
				.signatureDuration(PUT_IMAGE_DURATION)
				.putObjectRequest(putObjectRequest)
				.build();

			var presignedPutObjectRequest = s3Presigner.presignPutObject(presignedRequest);
			return presignedPutObjectRequest.url().toExternalForm();
		}
	}

	private String createPresignedGetUrl(String imageKey) {
		try (var s3Presigner = createS3Presigner()) {
			var getObjectRequest = GetObjectRequest.builder()
				.bucket(s3Property.bucketName())
				.key(imageKey)
				.build();

			var presignedRequest = GetObjectPresignRequest.builder()
				.signatureDuration(PUT_IMAGE_DURATION)
				.getObjectRequest(getObjectRequest)
				.build();

			var presignedGetObjectRequest = s3Presigner.presignGetObject(presignedRequest);
			return presignedGetObjectRequest.url().toExternalForm();
		}
	}

	private S3Presigner createS3Presigner() {
		return S3Presigner.builder()
			.credentialsProvider(staticCredentialsProvider)
			.endpointOverride(URI.create(s3Property.endpoint()))
			.region(Region.of(s3Property.region()))
			.build();
	}
}
