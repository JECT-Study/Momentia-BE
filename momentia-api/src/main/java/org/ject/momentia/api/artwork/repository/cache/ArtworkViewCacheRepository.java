package org.ject.momentia.api.artwork.repository.cache;

import org.ject.momentia.api.artwork.repository.cache.model.ArtworkViewCacheModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkViewCacheRepository extends CrudRepository<ArtworkViewCacheModel, Long> {

}
