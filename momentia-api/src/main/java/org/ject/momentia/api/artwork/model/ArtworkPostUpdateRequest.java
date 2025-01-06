package org.ject.momentia.api.artwork.model;
import jakarta.validation.constraints.Size;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.common.domain.artwork.type.ArtworkPostStatus;
import org.ject.momentia.common.domain.artwork.type.Category;

public record ArtworkPostUpdateRequest(

        @Size(min = 1, max = 50)
        String title,

        @Size(max = 1000)
        String explanation,

        @EnumValue(enumClass = Category.class, nullable = true)
        String artworkField,

        @EnumValue(enumClass = ArtworkPostStatus.class, ignoreCase = true,nullable = true)
        String status
) {
}
