package org.ject.momentia.api.image.converter;

import org.ject.momentia.api.image.entity.TempImage;
import org.ject.momentia.api.image.model.type.ImageStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TempImageConverter {
	public static TempImage presignedOf(String presignedUrl) {
		return TempImage.builder()
			.presignedUrl(presignedUrl)
			.status(ImageStatus.PENDING)
			.build();
	}
}
