package org.ject.momentia.api.collection.service;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.artwork.service.ArtworkService;
import org.ject.momentia.api.collection.converter.CollectionArtworkConverter;
import org.ject.momentia.api.collection.model.CollectionArtworkCreateResponse;
import org.ject.momentia.api.collection.repository.CollectionArtworkRepository;
import org.ject.momentia.api.collection.repository.CollectionRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CollectionArtworkService {

    private final CollectionArtworkRepository collectionArtworkRepository;
    private final ArtworkPostRepository artworkPostRepository;

    private final CollectionService collectionService;

    public CollectionArtworkCreateResponse create(Long collectionId, Long artworkId, User user){
        Collection collection = collectionService.findCollectionElseThrowException(collectionId);
        hasPermissionAtCollectionElseThrowException(collection, user);
        ArtworkPost artwork = findArtworkByIdElseThrowException(artworkId);
        CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection,artwork);
        if(collectionArtworkRepository.existsById(collectionArtwork.getId())) throw ErrorCd.DUPLICATE_COLLECTION_ARTWORK.serviceException();
        collectionArtworkRepository.save(collectionArtwork);
        return new CollectionArtworkCreateResponse(collection.getId(),artwork.getId());
    }


    public void delete(Long collectionId, Long artworkId, User user) {
        Collection collection = collectionService.findCollectionElseThrowException(collectionId);
        hasPermissionAtCollectionElseThrowException(collection, user);
        ArtworkPost artwork = findArtworkByIdElseThrowException(artworkId);
        CollectionArtwork collectionArtwork = CollectionArtworkConverter.toCollectionArtwork(collection,artwork);
        if(!collectionArtworkRepository.existsById(collectionArtwork.getId())) throw ErrorCd.COLLECTION_ARTWORK_ALREADY_REMOVED.serviceException();
        collectionArtworkRepository.delete(collectionArtwork);
    }

    public ArtworkPost findArtworkByIdElseThrowException(Long artworkId) {
        return artworkPostRepository.findById(artworkId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
    }


    public void hasPermissionAtCollectionElseThrowException(Collection collection, User user) {
        if(user == null || !Objects.equals(collection.getUser().getId(), user.getId())){ErrorCd.NO_PERMISSION.serviceException();}
    }


    public void deleteAllArtworksInCollection(ArtworkPost post) {
        collectionArtworkRepository.deleteAllByArtworkPost(post);
    }

}
