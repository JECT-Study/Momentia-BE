package org.ject.momentia.api.artwork.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtworkCommentRequest(
        @NotNull
        @NotEmpty
        String content
) {
}
