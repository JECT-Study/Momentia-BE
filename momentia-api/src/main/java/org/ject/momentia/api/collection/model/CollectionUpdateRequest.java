package org.ject.momentia.api.collection.model;

import jakarta.validation.constraints.Size;
import org.ject.momentia.api.global.annotation.EnumValue;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;

public record CollectionUpdateRequest (
        @Size(min=1,max = 50)
        String name,

        @EnumValue(enumClass = CollectionStatus.class, nullable = true)
        String status
){
}
