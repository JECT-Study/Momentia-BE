package org.ject.momentia.api.collection.service.module;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.collection.repository.CollectionArtworkRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@AllArgsConstructor
public class CollectionArtworkModuleService {

    private final CollectionArtworkRepository collectionArtworkRepository;

    public void deleteAllByCollection(Collection collection) {
        collectionArtworkRepository.deleteAllByCollection(collection);
    }

    public void hasPermissionAtCollectionElseThrowException(Collection collection, User user) {
        if(user == null || !Objects.equals(collection.getUser().getId(), user.getId())){
            ErrorCd.NO_PERMISSION.serviceException();}
    }

    public void deleteAllArtworksInCollection(ArtworkPost post) {
        collectionArtworkRepository.deleteAllByArtworkPost(post);
    }

    public ArtworkPost  findByArtworkPostByCollectionIdElseReturnNull(User user, Long collectionId) {
        return collectionArtworkRepository.findByArtworkPostByCollectionId(collectionId,user).orElse(null);
    }
}
