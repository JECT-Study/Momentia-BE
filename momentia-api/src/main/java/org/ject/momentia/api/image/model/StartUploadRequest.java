package org.ject.momentia.api.image.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public record StartUploadRequest(
	@PositiveOrZero Long fileSize,
	@NotEmpty String fileType
) {
}

