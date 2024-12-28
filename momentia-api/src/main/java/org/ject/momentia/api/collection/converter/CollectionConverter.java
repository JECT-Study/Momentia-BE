package org.ject.momentia.api.collection.converter;

import org.ject.momentia.api.collection.model.CollectionCreateResquest;
import org.ject.momentia.api.collection.model.CollectionListModel;
import org.ject.momentia.common.domain.collection.Collection;
import org.ject.momentia.common.domain.collection.CollectionArtwork;
import org.ject.momentia.common.domain.collection.type.CollectionStatus;
import org.ject.momentia.common.domain.user.User;

public class CollectionConverter {

    public static Collection toCollection(CollectionCreateResquest collectionCreateResquest, User user){
        return Collection.builder()
                .name(collectionCreateResquest.name())
                .status(CollectionStatus.valueOf(collectionCreateResquest.status()))
                .user(user)
                .build();
    }

    public static CollectionListModel toCollectionListModel(Collection collection, String imageUrl){
        return CollectionListModel.builder()
                .collectionId(collection.getId())
                .collectionImage(imageUrl)
                .status(collection.getStatus().name())
                .name(collection.getName())
                .build();
    }
}
