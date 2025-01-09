package org.ject.momentia.api.collection.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.collection.converter.CollectionArtworkConverter;
import org.ject.momentia.api.collection.model.CollectionArtworkCreateResponse;
import org.ject.momentia.api.collection.repository.CollectionArtworkRepository;
import org.ject.momentia.api.collection.service.module.CollectionArtworkModuleService;
import org.ject.momentia.api.collection.service.module.CollectionModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectionArtworkService {

    private final CollectionArtworkRepository collectionArtworkRepository;

    private final ArtworkPostModuleService artworkService;
    private final CollectionArtworkModuleService collectionArtworkModuleService;
    private final CollectionModuleService collectionService;

    @Transactional
    public CollectionArtworkCreateResponse create(Long collectionId, Long artworkId, User user){
        Collection collection = collectionService.findCollectionElseThrowException(collectionId);
        collectionArtworkModuleService.hasPermissionAtCollectionElseThrowException(collection, user);
        ArtworkPost artwork = artworkService.findPostByIdElseThrowError(artworkId);
        CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection,artwork);
        if(collectionArtworkRepository.existsById(collectionArtwork.getId())) throw ErrorCd.DUPLICATE_COLLECTION_ARTWORK.serviceException();
        collectionArtworkRepository.save(collectionArtwork);
        return new CollectionArtworkCreateResponse(collection.getId(),artwork.getId());
    }


    @Transactional
    public void delete(Long collectionId, Long artworkId, User user) {
        Collection collection = collectionService.findCollectionElseThrowException(collectionId);
        collectionArtworkModuleService.hasPermissionAtCollectionElseThrowException(collection, user);
        ArtworkPost artwork =artworkService.findPostByIdElseThrowError(artworkId);
        CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection,artwork);
        if(!collectionArtworkRepository.existsById(collectionArtwork.getId())) throw ErrorCd.COLLECTION_ARTWORK_ALREADY_REMOVED.serviceException();
        collectionArtworkRepository.delete(collectionArtwork);
    }

}
