package org.ject.momentia.api.collection.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.collection.converter.CollectionConverter;
import org.ject.momentia.api.collection.model.*;
import org.ject.momentia.api.collection.repository.CollectionRepository;
import org.ject.momentia.api.collection.service.module.CollectionArtworkModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

    private final ImageService imageService;
    private final CollectionArtworkModuleService collectionArtworkService;

    @Transactional
    public CollectionIdResponse createCollection(CollectionCreateResquest request, User user) {
        Collection collection = CollectionConverter.toCollection(request,user);

        // 이름 중복 체크
        if(collectionRepository.existsCollectionsByNameAndUser(request.name(),user)){
            throw ErrorCd.DUPLICATE_COLLECTION_NAME.serviceException();
        }

        collection = collectionRepository.save(collection);
        return new CollectionIdResponse(collection.getId());
    }


    @Transactional
    public void deleteCollection(Long collectionId, User user) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(ErrorCd.COLLECTION_NOT_FOUND::serviceException);
        if(user == null ||!collection.getUser().getId().equals(user.getId())) throw ErrorCd.NO_PERMISSION.serviceException();
        collectionRepository.delete(collection);
        collectionArtworkService.deleteAllByCollection(collection);
    }

    @Transactional
    public void updateCollection(Long collectionId, CollectionUpdateRequest request,User user){
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(ErrorCd.COLLECTION_NOT_FOUND::serviceException);
        if(user == null ||!collection.getUser().getId().equals(user.getId())) throw ErrorCd.NO_PERMISSION.serviceException();
        collection.update(request.name(), request.status());
    }

    @Transactional
    public CollecionListResponse getAllCollections(User user){
        if(user==null) throw ErrorCd.NO_PERMISSION.serviceException();
        List<Collection> collectionList = collectionRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<CollectionListModel> collectionListModelList = new ArrayList<>();
        for (Collection collection : collectionList) {
            ArtworkPost collectionArtwork =
                    collectionArtworkService.findByArtworkPostByCollectionIdElseReturnNull(user, collection.getId());
            String postImage = null;
            if (collectionArtwork != null) {
                postImage = imageService.getImageUrl(ImageTargetType.ARTWORK, collectionArtwork.getId());
            }
            collectionListModelList.add(CollectionConverter.toCollectionListModel(collection, postImage));
        }
        return new CollecionListResponse(collectionListModelList);
    }



}
