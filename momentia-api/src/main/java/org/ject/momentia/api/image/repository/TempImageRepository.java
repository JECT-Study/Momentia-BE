package org.ject.momentia.api.image.repository;

import org.ject.momentia.common.domain.image.TempImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempImageRepository extends JpaRepository<TempImage, Long> {
}
