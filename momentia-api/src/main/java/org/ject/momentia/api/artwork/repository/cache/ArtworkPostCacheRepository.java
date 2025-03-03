package org.ject.momentia.api.artwork.repository.cache;

import org.ject.momentia.api.artwork.model.cache.ArtworkPostCacheModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkPostCacheRepository extends CrudRepository<ArtworkPostCacheModel, Long> {
}
