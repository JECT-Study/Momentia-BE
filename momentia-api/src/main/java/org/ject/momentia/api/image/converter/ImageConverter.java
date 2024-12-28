package org.ject.momentia.api.image.converter;

import org.ject.momentia.api.image.entity.Image;
import org.ject.momentia.api.image.entity.TempImage;
import org.ject.momentia.api.image.model.type.ImageTargetType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageConverter {
	public static Image ofTempImage(TempImage tempImage, ImageTargetType imageTargetType, Long targetId) {
		return Image.builder()
			.id(tempImage.getId())
			.targetType(imageTargetType)
			.targetId(targetId)
			.imageSrc(tempImage.getPresignedUrl())
			.build();
	}
}
