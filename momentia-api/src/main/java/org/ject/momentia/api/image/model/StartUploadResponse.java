package org.ject.momentia.api.image.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record StartUploadResponse(
	@Positive Long imageId,
	@NotEmpty String presignedUrl,
	@NotEmpty String expectedFileName
) {
}

