package org.ject.momentia.api.artwork.repository.cache;

import org.ject.momentia.api.artwork.model.cache.PageIdsCacheModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageIdsCacheRepository extends CrudRepository<PageIdsCacheModel, String> {
	void deleteAll();
}
