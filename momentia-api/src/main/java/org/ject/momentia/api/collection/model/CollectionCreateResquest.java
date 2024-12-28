package org.ject.momentia.api.collection.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.ject.momentia.api.global.annotation.EnumValue;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;

public record CollectionCreateResquest(
        @NotEmpty
        @NotNull
        String name,

        @NotEmpty
        @NotNull
        @EnumValue(enumClass = CollectionStatus.class, message = "유효하지 않은 status입니다.", ignoreCase = true)
        String status
) {
}
