package org.ject.momentia.api.image.service;

import java.util.UUID;

import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.converter.ImageConverter;
import org.ject.momentia.api.image.converter.TempImageConverter;
import org.ject.momentia.api.image.infra.S3Resolver;
import org.ject.momentia.api.image.model.StartUploadRequest;
import org.ject.momentia.api.image.model.StartUploadResponse;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.api.image.repository.ImageRepository;
import org.ject.momentia.api.image.repository.TempImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;
	private final TempImageRepository tempImageRepository;
	private final S3Resolver s3Resolver;

	@Transactional
	public StartUploadResponse startUpload(StartUploadRequest startUploadRequest) {
		var key = createKey();
		var presignedUrl = s3Resolver.startImageUpload(key, startUploadRequest.fileSize(),
			startUploadRequest.fileType());
		var tempImage = TempImageConverter.presignedOf(presignedUrl);

		tempImageRepository.save(tempImage);
		return new StartUploadResponse(tempImage.getId(), presignedUrl.getLeft(), key);
	}

	@Transactional
	public void completeUpload(Long imageId) {
		var tempImage = tempImageRepository.findById(imageId)
			.orElseThrow(ErrorCd.IMAGE_NOT_FOUND::serviceException);

		if (tempImage.isUploaded()) {
			throw ErrorCd.IMAGE_ALREADY_PROCESSED.serviceException();
		}

		tempImage.completeUpload();
	}

	@Transactional
	public void useImageToService(Long imageId, ImageTargetType targetType, Long targetId) {
		var tempImage = tempImageRepository.findById(imageId)
			.orElseThrow(ErrorCd.IMAGE_NOT_FOUND::serviceException);

		var image = ImageConverter.ofTempImage(tempImage, targetType, targetId);
		imageRepository.save(image);
		tempImageRepository.delete(tempImage);
	}

	public String getImageUrl(ImageTargetType targetType, Long targetId) {
		var image = imageRepository.findByTargetTypeAndTargetId(targetType, targetId)
				.orElseThrow(ErrorCd.IMAGE_NOT_FOUND::serviceException);
		return image.getImageSrc();
	}

	private String createKey() {
		return UUID.randomUUID().toString();
	}
}
