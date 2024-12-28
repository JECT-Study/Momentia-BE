package org.ject.momentia.api.image.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.ject.momentia.common.domain.image.TempImage;
import org.ject.momentia.common.domain.image.type.ImageStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TempImageConverter {
	public static TempImage presignedOf(Pair<String, String> presignedUrl) {
		return TempImage.builder()
			.presignedPutUrl(presignedUrl.getLeft())
			.presignedGetUrl(presignedUrl.getRight())
			.status(ImageStatus.PENDING)
			.build();
	}
}
