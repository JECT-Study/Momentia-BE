package org.ject.momentia.api.image.repository;

import org.ject.momentia.common.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
