package org.ject.momentia.api.image.converter;

import org.ject.momentia.common.domain.image.Image;
import org.ject.momentia.common.domain.image.TempImage;
import org.ject.momentia.common.domain.image.type.ImageTargetType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageConverter {
	public static Image ofTempImage(TempImage tempImage, ImageTargetType imageTargetType, Long targetId) {
		return Image.builder()
			.id(tempImage.getId())
			.targetType(imageTargetType)
			.targetId(targetId)
			.imageSrc(tempImage.getPresignedGetUrl())
			.build();
	}
}
