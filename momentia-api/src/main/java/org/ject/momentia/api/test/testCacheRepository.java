package org.ject.momentia.api.test;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface testCacheRepository extends CrudRepository<testCacheModel, Long> {
}
