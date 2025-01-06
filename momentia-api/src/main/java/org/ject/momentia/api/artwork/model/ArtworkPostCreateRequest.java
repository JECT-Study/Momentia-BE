package org.ject.momentia.api.artwork.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ject.momentia.api.mvc.annotation.EnumValue;
import org.ject.momentia.common.domain.artwork.type.Category;

public record ArtworkPostCreateRequest(
        @NotEmpty @Size(min = 1, max = 50)
        String title,

        // category 검증 어노테이션
        @NotEmpty
        @NotNull
        @EnumValue(enumClass = Category.class)
        String artworkField,

        @NotNull
        Long postImage,

        @Size(max = 1000)
        String explanation,

        @NotEmpty
        String status
){}

