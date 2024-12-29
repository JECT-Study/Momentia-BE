package org.ject.momentia.api.image.repository;

import org.ject.momentia.common.domain.image.Image;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByTargetTypeAndTargetId(ImageTargetType targetType, Long targetId);
}
