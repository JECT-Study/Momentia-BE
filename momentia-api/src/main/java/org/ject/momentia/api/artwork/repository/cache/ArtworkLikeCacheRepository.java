package org.ject.momentia.api.artwork.repository.cache;

import org.ject.momentia.api.artwork.repository.cache.model.ArtworkLikeCacheModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkLikeCacheRepository extends CrudRepository<ArtworkLikeCacheModel, Long> {
	Iterable<ArtworkLikeCacheModel> findAll();
}
