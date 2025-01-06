package org.ject.momentia.api.collection.service.module;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.collection.repository.CollectionRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.collection.Collection;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectionModuleService {
    private final CollectionRepository collectionRepository;
    public Collection findCollectionElseThrowException(Long collectionId) {
        return collectionRepository.findById(collectionId).orElseThrow(ErrorCd.COLLECTION_NOT_FOUND::serviceException);
    }
}
