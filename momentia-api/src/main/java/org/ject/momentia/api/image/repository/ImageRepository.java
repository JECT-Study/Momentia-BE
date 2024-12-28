package org.ject.momentia.api.image.repository;

import org.ject.momentia.api.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
