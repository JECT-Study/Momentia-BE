package org.ject.momentia.api.image.repository;

import org.ject.momentia.api.image.entity.TempImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempImageRepository extends JpaRepository<TempImage, Long> {
}
